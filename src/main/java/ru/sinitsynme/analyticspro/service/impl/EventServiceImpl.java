package ru.sinitsynme.analyticspro.service.impl;

import org.springframework.stereotype.Service;
import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.event.EventEntity;
import ru.sinitsynme.analyticspro.entity.event.EventType;
import ru.sinitsynme.analyticspro.exception.ResourceNotFoundException;
import ru.sinitsynme.analyticspro.mapper.EventMapper;
import ru.sinitsynme.analyticspro.repository.ApplicationRepository;
import ru.sinitsynme.analyticspro.repository.EventRepository;
import ru.sinitsynme.analyticspro.repository.EventTypeRepository;
import ru.sinitsynme.analyticspro.service.EventService;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ApplicationRepository applicationRepository;
    private final EventTypeRepository eventTypeRepository;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, ApplicationRepository applicationRepository, EventTypeRepository eventTypeRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.applicationRepository = applicationRepository;
        this.eventTypeRepository = eventTypeRepository;
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

        EventType eventType;

        Optional<EventType> optionalEventType = eventTypeRepository.findEventTypeByNameAndApplication(event.getName(), application);

        if(optionalEventType.isEmpty()){
            eventType = new EventType(event.getName());
            eventType.setApplication(application);
        }
        else eventType = optionalEventType.get();


        event.setEventType(eventType);
        event.setApplication(application);
        event.setDate(Date.from(Instant.now()));

        return eventMapper.toDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> listAllApplicationEvents(Long applicationId) {
        if (!applicationRepository.existsById(applicationId))
            throw new ResourceNotFoundException(String.format("Application with ID = %d was not found", applicationId));

        ApplicationEntity application = applicationRepository.getById(applicationId);

        return eventRepository.findAllByApplication(application).stream()
                .map(eventMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDto> listApplicationEventsByFilter(List<EventType> eventTypes) {
        return eventRepository.findAllByEventTypeIn(eventTypes)
                .stream().map(eventMapper::toDto).collect(Collectors.toList());
    }

}
