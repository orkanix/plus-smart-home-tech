package ru.practicum.collector.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.props.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.props.key-deserializer}")
    private String keyDeserializerClass;

    @Value("${spring.kafka.props.consumer.value-deserializer}")
    private String valueDeserializerClass;

    @Bean
    public ConsumerFactory<String, SpecificRecordBase> consumerFactory() throws ClassNotFoundException {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Class.forName(keyDeserializerClass));
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Class.forName(valueDeserializerClass));

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

}
