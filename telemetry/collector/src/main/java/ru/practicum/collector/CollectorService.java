package ru.practicum.collector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.collector.HubEvents.HubEvent;
import ru.practicum.collector.SensorEvents.*;
import ru.practicum.collector.kafka.producer.KafkaEventProducer;
import ru.practicum.collector.kafka.mapper.sensor.*;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final KafkaEventProducer kafkaEventProducer;

    public void sendSensorEvent(SensorEvent event) {
        kafkaEventProducer.sendSensorEvent(event);
    }

    public void sendHubEvent(HubEvent event) {
        kafkaEventProducer.sendHubEvent(event);
    }
}
