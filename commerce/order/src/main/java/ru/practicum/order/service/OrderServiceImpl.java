package ru.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.interaction_api.delivery.client.DeliveryClient;
import ru.practicum.interaction_api.delivery.dto.DeliveryDto;
import ru.practicum.interaction_api.order.dto.OrderDto;
import ru.practicum.interaction_api.order.dto.OrderState;
import ru.practicum.interaction_api.payment.client.PaymentClient;
import ru.practicum.interaction_api.payment.dto.PaymentDto;
import ru.practicum.interaction_api.warehouse.ProductLowQuantityInWarehouse;
import ru.practicum.interaction_api.warehouse.client.WarehouseClient;
import ru.practicum.interaction_api.warehouse.dto.AssemblyProductsForOrderRequest;
import ru.practicum.interaction_api.warehouse.dto.BookedProductsDto;
import ru.practicum.order.exception.NoOrderFoundException;
import ru.practicum.order.exception.NotAuthorizedUserException;
import ru.practicum.order.exception.ProductReturnRequest;
import ru.practicum.order.model.CreateNewOrderRequest;
import ru.practicum.order.model.Order;
import ru.practicum.order.model.mapper.OrderMapper;
import ru.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;

    @Override
    public Page<OrderDto> getOrder(String username, Pageable pageable) {
        if (username == null) {
            throw new NotAuthorizedUserException("Поле username не может быть пустым!");
        }

        Page<Order> orders = repository.findByUsername(username, pageable);
        return orders.map(OrderMapper::toDto);
    }

    @Override
    public OrderDto getOrderByPayment(UUID paymentId) {
        return OrderMapper.toDto(repository.findByPaymentId(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ с id оплаты " + paymentId + " не найден!")));
    }

    @Override
    public OrderDto getOrderByDelivery(UUID deliveryId) {
        return OrderMapper.toDto(repository.findByDeliveryId(deliveryId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ с id доставки " + deliveryId + " не найден!")));
    }

    @Override
    public OrderDto createOrder(String username, CreateNewOrderRequest request) {

        if (username == null) {
            throw new NotAuthorizedUserException("Поле username не может быть пустым!");
        }

        Order newOrder = Order.builder()
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .products(request.getShoppingCart().getProducts())
                .username(username)
                .build();

        Order order = saveOrder(newOrder);

        try {
            //получение зарезервированных товаров
            BookedProductsDto bookedProducts = warehouseClient.assemblyProducts(AssemblyProductsForOrderRequest.builder()
                    .products(request.getShoppingCart().getProducts())
                    .orderId(order.getOrderId())
                    .build());

            //рассчет свойств товаров
            order.setDeliveryWeight(bookedProducts.getDeliveryWeight());
            order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
            order.setFragile(bookedProducts.getFragile());

            order.setProductPrice(paymentClient.calculateProductCost(OrderMapper.toDto(order)));

            // доставка
            DeliveryDto delivery = deliveryClient.createDelivery(
                    DeliveryDto.builder()
                            .fromAddress(warehouseClient.getWarehouseAddress())
                            .toAddress(request.getDeliveryAddress())
                            .orderId(order.getOrderId())
                            .build()
            );
            order.setDeliveryId(delivery.getDeliveryId());

            // расчет стоимости доставки
            BigDecimal deliveryPrice =
                    deliveryClient.calculateDeliveryCost(OrderMapper.toDto(order));
            order.setDeliveryPrice(deliveryPrice);

            // расчет итоговой стоимости
            BigDecimal totalPrice =
                    paymentClient.calculateTotalCost(OrderMapper.toDto(order));
            order.setTotalPrice(totalPrice);

            //оплата
            PaymentDto payment = paymentClient.goToPayment(OrderMapper.toDto(order));
            order.setPaymentId(payment.getPaymentId());

            repository.save(order);

            //меняем статус оплаты на успешный
            paymentClient.refund(payment.getPaymentId());

            return OrderMapper.toDto(orderExists(order.getOrderId()));
        } catch (ProductLowQuantityInWarehouse e) {
            repository.delete(order);
            throw new ProductLowQuantityInWarehouse(e.getMessage());
        } catch (Exception e) {
            repository.delete(order);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {

        Order order = orderExists(request.getOrderId());

        warehouseClient.returnProducts(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto paymentOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setState(OrderState.PAID);

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto failedPaymentOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setState(OrderState.PAYMENT_FAILED);

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto deliveryOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setState(OrderState.DELIVERED);

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto failedDeliveryOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setState(OrderState.DELIVERY_FAILED);

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto completedOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setState(OrderState.COMPLETED);

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto calculateTotalOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setTotalPrice(paymentClient.calculateTotalCost(OrderMapper.toDto(order)));

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setDeliveryPrice(deliveryClient.calculateDeliveryCost(OrderMapper.toDto(order)));

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto assemblyOrder(UUID orderId) {

        Order order = orderExists(orderId);
        order.setState(OrderState.ASSEMBLED);

        return OrderMapper.toDto(repository.save(order));
    }

    @Override
    public OrderDto failedAssemblyOrder(UUID orderId) {
        Order order = orderExists(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);

        return OrderMapper.toDto(repository.save(order));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Order saveOrder(Order newOrder) {
        return repository.save(newOrder);
    }

    private Order orderExists(UUID orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ с id " + orderId + " не найден!"));
    }

    public OrderState getActualState(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow();
        return order.getState();
    }
}
