package ru.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.analyzer.exception.EntityNotFoundException;
import ru.practicum.analyzer.model.*;
import ru.practicum.analyzer.repository.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubEventService {

    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;

    public void saveHubEvent(ConsumerRecords<String, HubEventAvro> hubEventList) {
        hubEventList.forEach(record -> {
            HubEventAvro event = record.value();
            Object payload = event.getPayload();

            if (payload instanceof DeviceAddedEventAvro deviceAdded) {
                saveDevice(deviceAdded.getId(), event.getHubId());
                log.info("");
                log.info("Сенсор {} успешно доабвлен в хаб {}!", deviceAdded.getId(), event.getHubId());
                log.info(deviceAdded.toString());
                log.info(sensorRepository.findAll().toString());
                log.info("");
            }

            if (payload instanceof ScenarioAddedEventAvro scenarioAdded) {
                saveScenario(event, scenarioAdded);
                log.info("Сценарий {} успешно добавлен!", scenarioAdded.getName());
            }

            if (payload instanceof DeviceRemovedEventAvro deviceRemoved) {
                removeDevice(deviceRemoved.getId(), event.getHubId());
                log.info("Сенсор {} успешно удален!", deviceRemoved.getId());
            }

            if (payload instanceof ScenarioRemovedEventAvro scenarioRemoved) {
                removeScenario(scenarioRemoved.getName(), event.getHubId());
                log.info("Сценарий {} успешно удален!", scenarioRemoved.getName());
            }
        });
    }

    @Transactional
    private void saveDevice(String sensorId, String hubId) {
        Sensor sensor = Sensor.builder()
                .id(sensorId)
                .hubId(hubId)
                .build();
        sensorRepository.save(sensor);
    }

    @Transactional
    private void saveScenario(HubEventAvro event, ScenarioAddedEventAvro added) {
        Scenario newScenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(added.getName())
                .build();

        Scenario savedScenario = scenarioRepository.save(newScenario);

        added.getConditions().forEach(condition -> {
            Integer value = null;

            if (condition.getValue() instanceof Boolean boolVal) {
                value = boolVal ? 1 : 0;
            } else if (condition.getValue() instanceof Integer intVal) {
                value = intVal;
            }

            Condition newCondition = Condition.builder()
                    .type(condition.getType().toString())
                    .operation(condition.getOperation().toString())
                    .value(value)
                    .build();

            Condition savedCondition = conditionRepository.save(newCondition);

            Sensor sensor = sensorRepository.findByIdAndHubId(condition.getSensorId(), event.getHubId())
                    .orElseThrow(() -> new EntityNotFoundException("Сенсор " + condition.getSensorId() + " не найден"));

            ScenarioConditionId sensorId = ScenarioConditionId.builder()
                    .scenarioId(savedScenario.getId())
                    .sensorId(sensor.getId())
                    .conditionId(savedCondition.getId())
                    .build();

            ScenarioCondition scenarioCondition = ScenarioCondition.builder()
                    .id(sensorId)
                    .scenario(savedScenario)
                    .sensor(sensor)
                    .condition(savedCondition)
                    .build();

            scenarioConditionRepository.save(scenarioCondition);
        });

        added.getActions().forEach(action -> {
            Action newAction = Action.builder()
                    .type(action.getType().toString())
                    .value(action.getValue())
                    .build();

            Action saveAction = actionRepository.save(newAction);

            Sensor sensor = sensorRepository.findByIdAndHubId(action.getSensorId(), event.getHubId())
                    .orElseThrow(() -> new EntityNotFoundException("Сенсор " + action.getSensorId() + " не найден"));

            ScenarioActionId actionId = ScenarioActionId.builder()
                    .scenarioId(savedScenario.getId())
                    .sensorId(sensor.getId())
                    .actionId(saveAction.getId())
                    .build();

            ScenarioAction scenarioAction = ScenarioAction.builder()
                    .id(actionId)
                    .scenario(savedScenario)
                    .sensor(sensor)
                    .action(saveAction)
                    .build();

            scenarioActionRepository.save(scenarioAction);
        });
    }

    @Transactional
    private void removeDevice(String sensorId, String hubId) {
        Sensor sensor = sensorRepository.findByIdAndHubId(sensorId, hubId)
                .orElseThrow(() -> new EntityNotFoundException("Сенсор " + sensorId + " не найден"));
        sensorRepository.delete(sensor);
    }

    @Transactional
    private void removeScenario(String name, String hubId) {
        Scenario scenario = scenarioRepository.findByHubIdAndName(name, hubId)
                .orElseThrow(() -> new EntityNotFoundException("Сценарий " + name + " не найден"));
        scenarioRepository.delete(scenario);
    }
}
