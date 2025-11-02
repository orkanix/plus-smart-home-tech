package ru.practicum.collector.kafka.mapper.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.collector.SensorEvents.SwitchSensor;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@RequiredArgsConstructor
public class SwitchSensorMapper implements AvroMapper<SwitchSensor, SwitchSensorAvro> {

    @Override
    public SwitchSensorAvro toAvro(SwitchSensor javaObject) {

        return SwitchSensorAvro.newBuilder()
                .setState(javaObject.isState())
                .build();
    }
}
