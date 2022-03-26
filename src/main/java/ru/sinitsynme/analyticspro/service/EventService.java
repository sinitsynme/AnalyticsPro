package ru.sinitsynme.analyticspro.service;

import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.dto.filter.EventFilterDto;

import java.util.List;

public interface EventService {

    EventDto addEvent(EventDto eventDto);

    List<EventDto> listAllApplicationEvents(Long applicationId);

    List<EventDto> listApplicationEventsByFilter(EventFilterDto eventFilterDto);

    List<List<Object>> formEventLineDiagramData(EventFilterDto eventFilterDto);
}
