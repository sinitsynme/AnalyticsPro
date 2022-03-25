package ru.sinitsynme.analyticspro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.analyticspro.entity.ApplicationEntity;
import ru.sinitsynme.analyticspro.entity.event.EventType;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long> {

    Optional<EventType> findEventTypeByNameAndApplication(String name, ApplicationEntity application);

    List<EventType> findAllByApplication(ApplicationEntity application);

}
