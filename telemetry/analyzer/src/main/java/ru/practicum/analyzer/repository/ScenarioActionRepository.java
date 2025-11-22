package ru.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.analyzer.model.ScenarioAction;

import java.util.List;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, Long> {

    List<ScenarioAction> findAllByScenarioIdIn(List<Long> scenarioIds);
}
