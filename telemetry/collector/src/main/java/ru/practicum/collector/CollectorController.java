package ru.practicum.collector;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.collector.HubEvents.HubEvent;
import ru.practicum.collector.SensorEvents.SensorEvent;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService service;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        service.sendSensorEvent(sensorEvent);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        service.sendHubEvent(hubEvent);
    }
}
