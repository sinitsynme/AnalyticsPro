package ru.sinitsynme.analyticspro.service;

import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.entity.EventEntity;

public interface EventService {

    EventDto addEvent(EventDto eventDto);


}
