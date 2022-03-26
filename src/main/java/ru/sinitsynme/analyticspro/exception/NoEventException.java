package ru.sinitsynme.analyticspro.exception;

public class NoEventException extends RuntimeException{

    public NoEventException() {
    }

    public NoEventException(String message) {
        super(message);
    }
}
