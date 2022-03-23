package ru.sinitsynme.analyticspro.mapper;

import ru.sinitsynme.analyticspro.dto.EventDto;
import ru.sinitsynme.analyticspro.entity.EventEntity;

public interface EventMapper {

    EventDto toDto(EventEntity entity);

    EventEntity toEntity(EventDto dto);

}
