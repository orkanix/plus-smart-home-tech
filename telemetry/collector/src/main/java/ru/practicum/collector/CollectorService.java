package ru.practicum.collector;

import org.springframework.stereotype.Service;
import ru.practicum.collector.HubEvents.HubEvent;
import ru.practicum.collector.SensorEvents.SensorEvent;
import ru.practicum.collector.kafka.KafkaProducerService;

@Service
public class CollectorService {

    private final KafkaProducerService kafkaProducerService;

    public CollectorService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void sendHubEvent(HubEvent event) {
        kafkaProducerService.sendHubEvent(event);
    }

    public void sendSensorEvent(SensorEvent event) {
        kafkaProducerService.sendSensorEvent(event);
    }
}
