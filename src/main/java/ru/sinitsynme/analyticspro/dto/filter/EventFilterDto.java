package ru.sinitsynme.analyticspro.dto.filter;

import ru.sinitsynme.analyticspro.entity.event.EventType;

import java.util.List;

public class EventFilterDto {

    private List<EventType> filter;

    public EventFilterDto() {
    }

    public EventFilterDto(List<EventType> filter) {
        this.filter = filter;
    }

    public List<EventType> getFilter() {
        return filter;
    }

    public void setFilter(List<EventType> filter) {
        this.filter = filter;
    }
}
