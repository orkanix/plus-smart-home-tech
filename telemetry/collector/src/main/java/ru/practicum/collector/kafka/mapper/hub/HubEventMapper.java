package ru.practicum.collector.kafka.mapper.hub;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;
import ru.practicum.collector.events.hub.*;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
public class HubEventMapper implements AvroMapper<HubEvent, HubEventAvro> {

    private final DeviceAddedEventMapper deviceAddedEventMapper;
    private final DeviceRemovedEventMapper deviceRemovedEventMapper;
    private final ScenarioAddedEventMapper scenarioAddedEventMapper;
    private final ScenarioRemovedEventMapper scenarioRemovedEventMapper;

    @Override
    public HubEventAvro toAvro(HubEvent javaObject) {

        SpecificRecord payload = createPayload(javaObject);

        return HubEventAvro.newBuilder()
                .setHubId(javaObject.getHubId())
                .setTimestamp(javaObject.getTimestamp())
                .setPayload(payload)
                .build();
    }

    private SpecificRecord createPayload(HubEvent hubEvent) {
        return switch (hubEvent.getType()) {
            case DEVICE_ADDED_EVENT -> deviceAddedEventMapper.toAvro((DeviceAddedEvent) hubEvent);
            case DEVICE_REMOVE_EVENT -> deviceRemovedEventMapper.toAvro((DeviceRemovedEvent) hubEvent);
            case SCENARIO_ADDED_EVENT -> scenarioAddedEventMapper.toAvro((ScenarioAddedEvent) hubEvent);
            case SCENARIO_REMOVE_EVENT -> scenarioRemovedEventMapper.toAvro((ScenarioRemovedEvent) hubEvent);
        };
    }
}
