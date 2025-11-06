package ru.practicum.collector.kafka.mapper.hub;

import org.springframework.stereotype.Component;
import ru.practicum.collector.events.hub.ScenarioRemovedEvent;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventMapper implements AvroMapper<ScenarioRemovedEvent, ScenarioRemovedEventAvro> {

    @Override
    public ScenarioRemovedEventAvro toAvro(ScenarioRemovedEvent javaObject) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(javaObject.getName())
                .build();
    }
}
