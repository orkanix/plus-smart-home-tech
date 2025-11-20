package ru.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.analyzer.model.Condition;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
