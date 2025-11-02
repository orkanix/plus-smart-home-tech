package ru.practicum.collector.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.collector.HubEvents.HubEvent;
import ru.practicum.collector.SensorEvents.SensorEvent;
import ru.practicum.collector.kafka.AvroSerializer;
import ru.practicum.collector.kafka.mapper.AvroMapper;
import ru.practicum.collector.kafka.mapper.hub.HubEventMapper;
import ru.practicum.collector.kafka.mapper.sensor.SensorEventMapper;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final AvroSerializer avroSerializer;

    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;

    public <T, A extends SpecificRecordBase> void sendEvent(
            T javaObject,
            AvroMapper<T, A> mapper,
            String topic) {
        try {
            A avroObject = mapper.toAvro(javaObject);

            byte[] avroBytes = avroSerializer.serialize(avroObject);

            kafkaTemplate.send(topic, avroBytes)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Ошибка отправки события в Kafka топик {}: {}",
                                    topic, exception.getMessage(), exception);
                        } else {
                            log.info("Событие успешно отправлено в топик: {}, партиция: {}, offset: {}",
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        }
                    });

        } catch (IOException e) {
            log.error("Ошибка сериализации события: {}", e.getMessage(), e);
            throw new RuntimeException("Не удалось сериализовать событие", e);
        }
    }

    public void sendSensorEvent(SensorEvent sensorEvent) {
        String sensorsTopic = "telemetry.sensors.v1";
        sendEvent(sensorEvent, sensorEventMapper, sensorsTopic);
        log.info("Сообщение успешно отправлено!");
    }

    public void sendHubEvent(HubEvent hubEvent) {
        String hubsTopic = "telemetry.hubs.v1";
        sendEvent(hubEvent, hubEventMapper, hubsTopic);
    }
}
