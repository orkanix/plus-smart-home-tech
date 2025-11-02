package ru.practicum.collector.kafka.mapper.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.collector.SensorEvents.ClimateSensor;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
@RequiredArgsConstructor
public class ClimateSensorMapper implements AvroMapper<ClimateSensor, ClimateSensorAvro> {

    @Override
    public ClimateSensorAvro toAvro(ClimateSensor javaObject) {

        return ClimateSensorAvro.newBuilder()
                .setCo2Level(javaObject.getCo2_level())
                .setHumidity(javaObject.getHumidity())
                .setTemperatureC(javaObject.getTemperature_c())
                .build();
    }
}
