package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class NameNotFoundException extends RuntimeException {
    public NameNotFoundException() {
        super("name must be present");
    }

    public NameNotFoundException(String message) {
        super(message);
    }
}
