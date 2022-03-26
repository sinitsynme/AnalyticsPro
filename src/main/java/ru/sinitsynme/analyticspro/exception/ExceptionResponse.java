package ru.sinitsynme.analyticspro.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Ответ с исключением")
public class ExceptionResponse {

    @Schema(description = "Время перехвата исключения")
    private Date occurrenceTime;

    @Schema(description = "Сообщение исключения")
    private String message;

    public ExceptionResponse(Date occurrenceTime, String message) {

        this.occurrenceTime = occurrenceTime;
        this.message = message;
    }

    public Date getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(Date occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
