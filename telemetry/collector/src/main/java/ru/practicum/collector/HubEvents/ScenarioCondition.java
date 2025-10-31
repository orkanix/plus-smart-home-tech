package ru.practicum.collector.HubEvents;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString(callSuper = true)
public class ScenarioCondition extends HubBase{
    private String sensor_id;
    private ConditionType type;
    private ConditionOperation operation;
    private Integer value = null;
}
