package ru.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.analyzer.model.ScenarioCondition;

import java.util.Collection;

public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, Long> {
    Collection<ScenarioCondition> findAllByScenarioId(Long id);
}
