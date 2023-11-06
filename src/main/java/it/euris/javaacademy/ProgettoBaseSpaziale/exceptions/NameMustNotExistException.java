package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class NameMustNotExistException extends RuntimeException{
    public NameMustNotExistException() {
        super("name must not be present");
    }

    public NameMustNotExistException(String message) {
        super(message);
    }
}
