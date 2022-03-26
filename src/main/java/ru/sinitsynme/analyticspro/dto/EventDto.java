package ru.sinitsynme.analyticspro.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Событие")
public class EventDto {

    @Schema(description = "Название события")
    private String name;

    @Schema(description = "Идентификатор приложения")
    private Long applicationId;

    @Schema(description = "Дополнительная информация")
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

    @Override
    public String toString() {
        return "EventDto{" +
                "name='" + name + '\'' +
                ", applicationId=" + applicationId +
                ", additionalData='" + additionalData + '\'' +
                '}';
    }
}
