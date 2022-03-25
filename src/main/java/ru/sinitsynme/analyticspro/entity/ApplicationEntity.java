package ru.sinitsynme.analyticspro.entity;

import ru.sinitsynme.analyticspro.entity.event.EventEntity;
import ru.sinitsynme.analyticspro.entity.event.EventType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "application")
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "application")
    private List<EventEntity> eventList;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "application")
    private List<EventType> eventTypeList;


    public ApplicationEntity() {
    }

    public ApplicationEntity(UserEntity user, String name) {
        this.user = user;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EventEntity> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventEntity> eventList) {
        this.eventList = eventList;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<EventType> getEventTypeList() {
        return eventTypeList;
    }

    public void setEventTypeList(List<EventType> eventTypeList) {
        this.eventTypeList = eventTypeList;
    }
}
