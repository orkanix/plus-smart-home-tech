package ru.practicum.collector.kafka.mapper;

import org.apache.avro.specific.SpecificRecordBase;

public interface AvroMapper<T, A extends SpecificRecordBase> {
    A toAvro(T javaObject);
}
