package ru.sinitsynme.analyticspro.exception;

public class ApplicationRegistrationException extends RuntimeException{

    public ApplicationRegistrationException() {
    }

    public ApplicationRegistrationException(String message) {
        super(message);
    }
}
