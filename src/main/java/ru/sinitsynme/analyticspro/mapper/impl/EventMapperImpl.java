package ru.sinitsynme.analyticspro.mapper.impl;

import org.springframework.stereotype.Component;
import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.entity.EventEntity;
import ru.sinitsynme.analyticspro.mapper.EventMapper;

@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDto toDto(EventEntity entity) {
        return new EventDto(entity.getName(), entity.getApplication().getId(), entity.getAdditionalData());
    }

    @Override
    public EventEntity toEntity(EventDto dto) {
        EventEntity event = new EventEntity();
        event.setAdditionalData(dto.getAdditionalData());
        event.setName(dto.getName());

        return event;
    }
}
