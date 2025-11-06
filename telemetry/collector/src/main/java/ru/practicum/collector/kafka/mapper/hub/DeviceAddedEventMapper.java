package ru.practicum.collector.kafka.mapper.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.events.hub.DeviceAddedEvent;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedEventMapper implements AvroMapper<DeviceAddedEvent, DeviceAddedEventAvro> {

    @Override
    public DeviceAddedEventAvro toAvro(DeviceAddedEvent javaObject) {
        return DeviceAddedEventAvro.newBuilder()
                .setId(javaObject.getId())
                .setType(getDeviceType(javaObject))
                .build();
    }

    private DeviceTypeAvro getDeviceType(DeviceAddedEvent javaObject) {
        return switch (javaObject.getDeviceType()) {
            case MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
        };
    }
}
