package ru.practicum.collector.HubEvents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString(callSuper = true)
public class DeviceAction {
    private String sensor_id;
    private ActionType type;
    private Integer value = null;
}
