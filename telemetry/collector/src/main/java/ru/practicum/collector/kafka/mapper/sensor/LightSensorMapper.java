package ru.practicum.collector.kafka.mapper.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.collector.events.sensor.LightSensor;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
@RequiredArgsConstructor
public class LightSensorMapper implements AvroMapper<LightSensor, LightSensorAvro> {

    @Override
    public LightSensorAvro toAvro(LightSensor javaObject) {

        return LightSensorAvro.newBuilder()
                .setLinkQuality(javaObject.getLinkQuality())
                .setLuminosity(javaObject.getLuminosity())
                .build();
    }
}
