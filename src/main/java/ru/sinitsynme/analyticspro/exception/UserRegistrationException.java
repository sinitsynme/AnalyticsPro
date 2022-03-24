package ru.sinitsynme.analyticspro.exception;

public class UserRegistrationException extends RuntimeException{

    public UserRegistrationException() {
    }

    public UserRegistrationException(String message) {
        super(message);
    }
}
