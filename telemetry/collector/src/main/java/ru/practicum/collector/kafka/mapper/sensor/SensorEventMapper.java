package ru.practicum.collector.kafka.mapper.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.collector.SensorEvents.*;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorEventMapper implements AvroMapper<SensorEvent, SensorEventAvro> {

    private final ClimateSensorMapper climateSensorMapper;
    private final LightSensorMapper lightSensorMapper;
    private final MotionSensorMapper motionSensorMapper;
    private final SwitchSensorMapper switchSensorMapper;
    private final TemperatureSensorMapper temperatureSensorMapper;

    @Override
    public SensorEventAvro toAvro(SensorEvent javaObject) {

        Object payloadObject = createPayload(javaObject);

        return SensorEventAvro.newBuilder()
                .setHubId(javaObject.getHubId())
                .setId(javaObject.getId())
                .setTimestamp(javaObject.getTimestamp())
                .setPayload(payloadObject)
                .build();
    }

    private Object createPayload(SensorEvent sensorEvent) {
        return switch (sensorEvent.getType()) {
            case CLIMATE_SENSOR_EVENT -> climateSensorMapper.toAvro((ClimateSensor) sensorEvent);
            case LIGHT_SENSOR_EVENT -> lightSensorMapper.toAvro((LightSensor) sensorEvent);
            case MOTION_SENSOR_EVENT -> motionSensorMapper.toAvro((MotionSensor) sensorEvent);
            case SWITCH_SENSOR_EVENT -> switchSensorMapper.toAvro((SwitchSensor) sensorEvent);
            case TEMPERATURE_SENSOR_EVENT -> temperatureSensorMapper.toAvro((TemperatureSensor) sensorEvent);
        };
    }
}
