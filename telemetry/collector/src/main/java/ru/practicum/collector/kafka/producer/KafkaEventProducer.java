package ru.practicum.collector.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.collector.events.hub.HubEvent;
import ru.practicum.collector.events.sensor.SensorEvent;
import ru.practicum.collector.kafka.mapper.hub.HubEventMapper;
import ru.practicum.collector.kafka.mapper.sensor.SensorEventMapper;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class KafkaEventProducer {

    protected final KafkaTemplate<String, SpecificRecordBase> producer;
    private final HubEventMapper hubEventMapper;
    private final SensorEventMapper sensorEventMapper;

    @Value("${spring.kafka.topics.hub-topic-name}")
    private String hubTopic;
    @Value("${spring.kafka.topics.sensor-topic-name}")
    private String sensorTopic;

    private void send(SpecificRecordBase event, String hubId, Instant timestamp, String topic) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                topic,
                null,
                timestamp.toEpochMilli(),
                hubId,
                event
        );

        producer.send(record);
        producer.flush();
    }

    public void sendHubEvent(HubEvent hubEvent) {
        SpecificRecordBase hubEventAvro = hubEventMapper.toAvro(hubEvent);
        send(hubEventAvro, hubEvent.getHubId(), hubEvent.getTimestamp(), hubTopic);
    }

    public void sendSensorEvent(SensorEvent sensorEvent) {
        SpecificRecordBase sensorEventAvro = sensorEventMapper.toAvro(sensorEvent);
        send(sensorEventAvro, sensorEvent.getHubId(), sensorEvent.getTimestamp(), sensorTopic);
    }
}
