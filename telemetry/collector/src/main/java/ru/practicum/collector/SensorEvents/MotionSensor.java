package ru.practicum.collector.SensorEvents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString(callSuper = true)
public class MotionSensor extends SensorEvent {
    private int link_quality;
    private boolean motion;
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
