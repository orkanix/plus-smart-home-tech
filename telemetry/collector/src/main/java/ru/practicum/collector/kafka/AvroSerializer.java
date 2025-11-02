package ru.practicum.collector.kafka;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class AvroSerializer {

    public <T extends SpecificRecordBase> byte[] serialize(T avroObject) throws IOException {
        SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(avroObject.getSchema());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);

        writer.write(avroObject, encoder);

        encoder.flush();
        outputStream.close();

        byte[] result = outputStream.toByteArray();

        log.debug("Сериализован Avro объект типа: {}, размер: {} байт",
                avroObject.getClass().getSimpleName(),
                result.length);

        return result;
    }
}
