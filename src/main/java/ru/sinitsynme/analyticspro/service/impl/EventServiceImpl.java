package ru.sinitsynme.analyticspro.service.impl;

import org.springframework.stereotype.Service;
import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.EventEntity;
import ru.sinitsynme.analyticspro.exception.ResourceNotFoundException;
import ru.sinitsynme.analyticspro.mapper.EventMapper;
import ru.sinitsynme.analyticspro.repository.ApplicationRepository;
import ru.sinitsynme.analyticspro.repository.EventRepository;
import ru.sinitsynme.analyticspro.service.EventService;

import java.sql.Date;
import java.time.Instant;
import java.util.Locale;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ApplicationRepository applicationRepository;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, ApplicationRepository applicationRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public EventDto addEvent(EventDto eventDto) {
        if(eventDto.getName() == null || eventDto.getName().trim().isEmpty())
            throw new ResourceNotFoundException("Your event needs to have name");

        if(eventDto.getApplicationId() == null){
            throw new ResourceNotFoundException("Your event needs to have applicationId");
        }

        Long appId = eventDto.getApplicationId();
        if (!applicationRepository.existsById(appId))
            throw new ResourceNotFoundException(String.format("Application with ID = %d was not found", appId));

        ApplicationEntity application = applicationRepository.getById(appId);

        EventEntity event = eventMapper.toEntity(eventDto);
        event.setName(event.getName().toUpperCase(Locale.ROOT));
        event.setApplication(application);
        event.setDate(Date.from(Instant.now()));

        return eventMapper.toDto(eventRepository.save(event));
    }
}
