package ru.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.analyzer.model.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
}
