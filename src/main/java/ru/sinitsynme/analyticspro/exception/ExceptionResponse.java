package ru.sinitsynme.analyticspro.exception;

import java.util.Date;

public class ExceptionResponse {
    private Date occurrenceTime;
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
