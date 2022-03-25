package ru.sinitsynme.analyticspro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.event.EventEntity;
import ru.sinitsynme.analyticspro.entity.event.EventType;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    List<EventEntity> findAllByEventType(EventType eventType);

    List<EventEntity> findAllByApplication(ApplicationEntity application);

    List<EventEntity> findAllByEventTypeIn(List<EventType> eventTypes);
}
