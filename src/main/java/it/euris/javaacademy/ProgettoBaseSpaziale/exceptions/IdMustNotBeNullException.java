package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class IdMustNotBeNullException extends RuntimeException{

    public IdMustNotBeNullException() {
        super("Id must not be null.");
    }

    public IdMustNotBeNullException(String message) {
        super(message);
    }

}

