package ru.practicum.collector.kafka.mapper.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.collector.SensorEvents.MotionSensor;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
@RequiredArgsConstructor
public class MotionSensorMapper implements AvroMapper<MotionSensor, MotionSensorAvro> {

    @Override
    public MotionSensorAvro toAvro(MotionSensor javaObject) {

        return MotionSensorAvro.newBuilder()
                .setMotion(javaObject.isMotion())
                .setLinkQuality(javaObject.getLinkQuality())
                .setVoltage(javaObject.getVoltage())
                .build();
    }
}
