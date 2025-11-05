package ru.practicum.collector.events.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioCondition{
    private String sensorId;
    private ConditionType conditionType;
    private ConditionOperation operation;
    private Integer value = null;
}
