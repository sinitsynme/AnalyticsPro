package ru.sinitsynme.analyticspro.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.service.EventService;

import javax.transaction.Transactional;
import java.util.List;

@Tag(name = "Управление событиями")
@RestController
@RequestMapping("/events")
public class EventRestController {

    private final EventService eventService;

    @Autowired
    public EventRestController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Зарегистрировать новое событие")
    @PostMapping
    public ResponseEntity<EventDto> registerEvent(@RequestBody EventDto eventDto){
        EventDto event = eventService.addEvent(eventDto);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @Operation(summary = "Зарегистрировать список новых событий")
    @PostMapping("/list")
    public ResponseEntity<?> registerListOfEvents(@RequestBody List<EventDto> eventDtos){
        eventService.addListOfEvents(eventDtos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
