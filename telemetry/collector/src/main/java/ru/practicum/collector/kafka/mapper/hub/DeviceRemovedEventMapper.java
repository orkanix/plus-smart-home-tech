package ru.practicum.collector.kafka.mapper.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.HubEvents.DeviceRemovedEvent;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventMapper implements AvroMapper<DeviceRemovedEvent, DeviceRemovedEventAvro> {

    @Override
    public DeviceRemovedEventAvro toAvro(DeviceRemovedEvent javaObject) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(javaObject.getId())
                .build();
    }
}
