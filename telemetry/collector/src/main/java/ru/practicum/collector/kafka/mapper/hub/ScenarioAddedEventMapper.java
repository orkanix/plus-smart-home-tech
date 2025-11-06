package ru.practicum.collector.kafka.mapper.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.collector.events.hub.ScenarioAddedEvent;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventMapper implements AvroMapper<ScenarioAddedEvent, ScenarioAddedEventAvro> {

    private final DeviceActionMapper deviceActionMapper;
    private final ScenarioConditionMapper conditionMapper;

    @Override
    public ScenarioAddedEventAvro toAvro(ScenarioAddedEvent javaObject) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(javaObject.getName())
                .setActions(getActions(javaObject))
                .setConditions(getScenarioConditions(javaObject))
                .build();
    }

    private List<DeviceActionAvro> getActions(ScenarioAddedEvent javaObject) {
        return javaObject.getActions().stream()
                .map(deviceActionMapper::toAvro)
                .toList();
    }

    private List<ScenarioConditionAvro> getScenarioConditions(ScenarioAddedEvent javaObject) {
        return javaObject.getConditions().stream()
                .map(conditionMapper::toAvro)
                .toList();
    }
}
