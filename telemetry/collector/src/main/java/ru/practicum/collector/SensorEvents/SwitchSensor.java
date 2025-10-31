package ru.practicum.collector.SensorEvents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString(callSuper = true)
public class SwitchSensor extends SensorEvent {
    private boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
