package ru.practicum.collector.kafka.mapper.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.collector.events.hub.DeviceAction;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

@Component
@Slf4j
public class DeviceActionMapper implements AvroMapper<DeviceAction, DeviceActionAvro> {

    @Override
    public DeviceActionAvro toAvro(DeviceAction javaObject) {
        log.info(javaObject.toString());
        return DeviceActionAvro.newBuilder()
                .setSensorId(javaObject.getSensorId())
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
