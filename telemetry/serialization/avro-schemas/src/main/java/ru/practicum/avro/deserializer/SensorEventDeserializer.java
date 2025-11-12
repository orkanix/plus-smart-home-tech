package ru.practicum.avro.deserializer;

import org.apache.avro.Schema;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class SensorEventDeserializer extends BaseAvroDeserializer<SensorEventAvro> {
    public SensorEventDeserializer(Schema schema) {
        super(schema);
    }

    public SensorEventDeserializer() {
        super(SensorEventAvro.getClassSchema()); // используем статический метод Avro
    }
}
