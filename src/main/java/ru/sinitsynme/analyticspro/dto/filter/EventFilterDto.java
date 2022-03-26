package ru.sinitsynme.analyticspro.dto.filter;

import ru.sinitsynme.analyticspro.entity.event.EventDateFilterType;
import ru.sinitsynme.analyticspro.entity.event.EventType;

import java.util.List;

public class EventFilterDto {

    private List<EventType> typeFilter;
    private EventDateFilterType dateFilter;

    public EventFilterDto() {
    }

    public EventFilterDto(List<EventType> typeFilter) {
        this.typeFilter = typeFilter;
    }

    public EventFilterDto(List<EventType> typeFilter, EventDateFilterType dateFilter) {
        this.typeFilter = typeFilter;
        this.dateFilter = dateFilter;
    }

    public List<EventType> getTypeFilter() {
        return typeFilter;
    }

    public void setTypeFilter(List<EventType> typeFilter) {
        this.typeFilter = typeFilter;
    }

    public EventDateFilterType getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(EventDateFilterType dateFilter) {
        this.dateFilter = dateFilter;
    }
}
