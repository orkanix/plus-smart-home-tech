package ru.practicum.collector.kafka.consumer;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Optional;

public class CheckUpdateState {

    public Optional<SensorsSnapshotAvro> updateState(List<SensorsSnapshotAvro> snapshotsList, SensorEventAvro event) {
        Optional<SensorsSnapshotAvro> snapshot = snapshotsList.stream()
                .filter(elem -> elem.getHubId().equals(event.getHubId()))
                .findFirst();

        if (snapshot.isPresent()) {
            SensorsSnapshotAvro snapshotAvro = snapshot.get();
        } else  {
            SensorsSnapshotAvro snapshotAvro = new SensorsSnapshotAvro();
        }

        return snapshot;
    }
}
