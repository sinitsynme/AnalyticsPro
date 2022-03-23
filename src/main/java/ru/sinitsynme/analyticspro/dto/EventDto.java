package ru.sinitsynme.analyticspro.dto;

public class EventDto {

    private String name;

    private Long applicationId;

    private String additionalData;

    public EventDto() {
    }

    public EventDto(String name, Long applicationId) {
        this.name = name;
        this.applicationId = applicationId;
    }

    public EventDto(String name, Long applicationId, String additionalData) {
        this.name = name;
        this.applicationId = applicationId;
        this.additionalData = additionalData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}
