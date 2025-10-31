package ru.practicum.collector;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.collector.HubEvents.HubEvent;
import ru.practicum.collector.SensorEvents.SensorEvent;

@RestController
@RequestMapping("/events")
public class CollectorController {
    private final CollectorService service;

    public CollectorController(CollectorService service) {
        this.service = service;
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        System.out.println(sensorEvent.getType());
        service.sendSensorEvent(sensorEvent);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent hubEvent) {
        service.sendHubEvent(hubEvent);
    }
}
