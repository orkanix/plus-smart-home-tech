package ru.practicum.collector.kafka.mapper.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.events.hub.ScenarioCondition;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Component
public class ScenarioConditionMapper implements AvroMapper<ScenarioCondition, ScenarioConditionAvro> {

    @Override
    public ScenarioConditionAvro toAvro(ScenarioCondition javaObject) {
        return ScenarioConditionAvro.newBuilder()
                .setValue(javaObject.getValue())
                .setSensorId(javaObject.getSensorId())
                .setType(getType(javaObject))
                .setOperation(getCondition(javaObject))
                .build();
    }

    private ConditionTypeAvro getType(ScenarioCondition javaObject) {
        return switch (javaObject.getType()) {
            case MOTION -> ConditionTypeAvro.MOTION;
            case LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case SWITCH -> ConditionTypeAvro.SWITCH;
            case TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case HUMIDITY -> ConditionTypeAvro.HUMIDITY;
        };
    }

    private ConditionOperationAvro getCondition(ScenarioCondition javaObject) {
        return switch (javaObject.getOperation()) {
            case EQUALS -> ConditionOperationAvro.EQUALS;
            case GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
        };
    }
}
