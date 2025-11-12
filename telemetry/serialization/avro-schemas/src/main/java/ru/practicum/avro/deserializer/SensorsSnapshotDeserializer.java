package ru.practicum.avro.deserializer;

import org.apache.avro.Schema;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Component
public class SensorsSnapshotDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {

    public SensorsSnapshotDeserializer(Schema schema) {
        super(schema);
    }

    public SensorsSnapshotDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema()); // используем статический метод Avro
    }
}
