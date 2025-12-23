package ru.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.interaction_api.delivery.client.DeliveryClient;
import ru.practicum.interaction_api.order.client.OrderClient;
import ru.practicum.interaction_api.shopping_cart.dto.ShoppingCartDto;
import ru.practicum.interaction_api.warehouse.ProductLowQuantityInWarehouse;
import ru.practicum.interaction_api.warehouse.dto.*;
import ru.practicum.warehouse.Warehouse;
import ru.practicum.warehouse.exception.*;
import ru.practicum.warehouse.model.*;
import ru.practicum.warehouse.model.mapper.OrderBookingMapper;
import ru.practicum.warehouse.model.mapper.ProductInWarehouseMapper;
import ru.practicum.warehouse.repository.OrderBookingRepository;
import ru.practicum.warehouse.repository.WarehouseRepository;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final OrderBookingRepository orderBookingRepository;

    private final OrderClient orderClient;
    private final DeliveryClient deliveryClient;

    @Override
    public ProductInWarehouseDto addNewProduct(NewProductInWarehouseRequest newProduct) {
        if (isProductInWarehouse(newProduct.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Продукт с id " + newProduct.getProductId() + " уже добавлен на склад!");
        }

        return ProductInWarehouseMapper.toDto(warehouseRepository.save(ProductInWarehouseMapper.toEntity(newProduct)));
    }

    @Override
    public BookedProductsDto checkQuantityForCart(ShoppingCartDto shoppingCart) {
        BookedProductsDto bookedProductsDto = BookedProductsDto.builder().build();

        shoppingCart.getProducts().forEach((productId, quantity) -> {
            ProductInWarehouse productInWarehouse = productInWarehouseExists(productId);

            if (quantity > productInWarehouse.getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товара с id " + productId + " в корзине больше, чем доступно на складе!");
            }

            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight()+ productInWarehouse.getWeight());
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume()+calculateVolume(productInWarehouse));
        });

        return bookedProductsDto;
    }

    @Override
    public void acceptProduct(AddProductToWarehouseRequest request) {

        ProductInWarehouse productInWarehouse = productInWarehouseExists(request.getProductId());
        productInWarehouse.setQuantity(productInWarehouse.getQuantity()+request.getQuantity());

        warehouseRepository.save(productInWarehouse);
        log.info(productInWarehouse.getProductId() + " " + productInWarehouse.getQuantity());
    }

    @Override
    public AddressDto getAddress() {
        return Warehouse.getRandomAddress();
    }

    @Override
    public void shippedProducts(ShippedToDeliveryRequest request) {

        OrderBooking orderBooking = orderBookingRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotOrderBookingFound("Забронированные товары для заказа с id " + request.getOrderId() + " не найдены!"));

        orderBooking.setDeliveryId(request.getDeliveryId());
        orderBookingRepository.save(orderBooking);

        log.info("Товары для заказа с id {} переданы в доставку!", request.getOrderId());
    }

    @Override
    @Transactional
    public void returnProducts(Map<UUID, Integer> products) {

        for (Map.Entry<UUID, Integer> entry : products.entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();

            try {
                ProductInWarehouse product = productInWarehouseExists(productId);

                product.setQuantity(product.getQuantity() + quantity);
                warehouseRepository.save(product);

            } catch (ProductInWarehouseNotFoundException e) {
                log.warn("Продукт с id {} не найден на складе, пропускаем!", productId);
            }
        }

        log.info("Товары успешно вернулись на склад!");
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request) {

        Double deliveryWeight = 0.0;
        double deliveryVolume = 0.0;
        boolean fragile = false;

        for(Map.Entry<UUID, Integer> entry : request.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Integer quantity = entry.getValue();

            try {
                ProductInWarehouse product = productInWarehouseExists(productId);

                if (product.getQuantity() < quantity) {
                    throw new ProductLowQuantityInWarehouse("Товара с id " + productId + " на складе меньше, чем запрашивается!");
                }

                product.setQuantity(product.getQuantity() - quantity);
                warehouseRepository.save(product);

                log.info(product.getQuantity().toString());

                deliveryWeight += product.getWeight();
                deliveryVolume += calculateVolume(product);

                if (product.getFragile()) {
                    fragile = true;
                }

            } catch (ProductInWarehouseNotFoundException e) {
                log.warn("Продукт с id {} не найден на складе, пропускаем!", productId);
            }
        }

        OrderBooking newOrderBooking = OrderBooking.builder()
                .products(request.getProducts())
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();

        orderClient.assemblyOrder(request.getOrderId());

        return OrderBookingMapper.toDto(orderBookingRepository.save(newOrderBooking));
    }

    private boolean isProductInWarehouse(UUID productId) {
        return warehouseRepository.existsById(productId);
    }

    private Double calculateVolume(ProductInWarehouse product) {
        DimensionDto dimension = product.getDimension();
        return dimension.getHeight()*dimension.getDepth()*dimension.getWidth();
    }

    private ProductInWarehouse productInWarehouseExists(UUID productId) {
        return warehouseRepository.findById(productId)
                .orElseThrow(() -> new ProductInWarehouseNotFoundException("Продукт с id " + productId + " не найден на складе!"));
    }
}
