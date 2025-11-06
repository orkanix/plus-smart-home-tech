package ru.practicum.collector.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.collector.events.hub.HubEvent;
import ru.practicum.collector.events.sensor.SensorEvent;
import ru.practicum.collector.kafka.producer.KafkaEventProducer;

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
