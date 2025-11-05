package ru.practicum.collector.kafka.mapper.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.collector.events.sensor.TemperatureSensor;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
@RequiredArgsConstructor
public class TemperatureSensorMapper implements AvroMapper<TemperatureSensor, TemperatureSensorAvro> {

    @Override
    public TemperatureSensorAvro toAvro(TemperatureSensor javaObject) {

        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(javaObject.getTemperatureC())
                .setTemperatureF(javaObject.getTemperatureF())
                .build();
    }
}
