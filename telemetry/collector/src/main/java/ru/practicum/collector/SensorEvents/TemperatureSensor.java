package ru.practicum.collector.SensorEvents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString(callSuper = true)
public class TemperatureSensor extends SensorEvent {
    private int temperature_c;
    private int temperature_f;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
