package ru.sinitsynme.analyticspro.service;

import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.entity.event.EventType;

import java.util.List;

public interface EventService {

    EventDto addEvent(EventDto eventDto);

    List<EventDto> listAllApplicationEvents(Long applicationId);

    List<EventDto> listApplicationEventsByFilter(List<EventType> eventTypes);
}
