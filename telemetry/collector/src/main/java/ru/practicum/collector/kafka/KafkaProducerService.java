package ru.practicum.collector.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import ru.practicum.collector.HubEvents.HubEvent;
import ru.practicum.collector.SensorEvents.SensorEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPOutputStream;

@Component
public class KafkaProducerService {

    private final KafkaProducer<String, byte[]> producer;
    private final ObjectMapper objectMapper;

    private static final String HUBS_TOPIC = "telemetry.hubs.v1";
    private static final String SENSORS_TOPIC = "telemetry.sensors.v1";

    public KafkaProducerService() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.ByteArraySerializer");

        this.producer = new KafkaProducer<>(props);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private void sendEvent(String topic, Object event, String eventName) {
        try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(event);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
                gzip.write(jsonBytes);
            }
            byte[] compressed = baos.toByteArray();

            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, compressed);
            RecordMetadata metadata = producer.send(record).get();

            System.out.printf(
                    "%s отправлен: topic=%s partition=%d offset=%d%n",
                    eventName, metadata.topic(), metadata.partition(), metadata.offset()
            );

        } catch (IOException e) {
            System.err.println("Ошибка сериализации " + eventName + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Отправка " + eventName + " прервана: " + e.getMessage());
        } catch (ExecutionException e) {
            System.err.println("Ошибка при отправке " + eventName + ": " + e.getCause().getMessage());
        }
    }

    public void sendHubEvent(HubEvent event) {
        sendEvent(HUBS_TOPIC, event, "HubEvent");
    }

    public void sendSensorEvent(SensorEvent event) {
        sendEvent(SENSORS_TOPIC, event, "SensorEvent");
    }

    @PreDestroy
    public void closeProducer() {
        producer.close();
    }
}
