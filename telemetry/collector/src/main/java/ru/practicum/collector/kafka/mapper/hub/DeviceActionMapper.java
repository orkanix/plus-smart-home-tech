package ru.practicum.collector.kafka.mapper.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.HubEvents.DeviceAction;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

@Component
public class DeviceActionMapper implements AvroMapper<DeviceAction, DeviceActionAvro> {

    @Override
    public DeviceActionAvro toAvro(DeviceAction javaObject) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(javaObject.getSensor_id())
                .setValue(javaObject.getValue())
                .setType(getType(javaObject))
                .build();
    }

    private ActionTypeAvro getType(DeviceAction javaObject) {
        return switch(javaObject.getType()) {
            case ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case INVERSE -> ActionTypeAvro.INVERSE;
            case SET_VALUE -> ActionTypeAvro.SET_VALUE  ;
        };
    }
}
