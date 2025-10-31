package ru.practicum.collector.HubEvents;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter @Setter @ToString
public abstract class HubBase {
    @NotBlank
    private String hubId;
    private Instant timestamp = Instant.now();
}
