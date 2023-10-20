package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class ForeignKeyIdMustNotBeNullException extends  RuntimeException{
    public ForeignKeyIdMustNotBeNullException() {
        super("Foreign id key must not be null");
    }
    public ForeignKeyIdMustNotBeNullException(String message) {
        super(message);
    }
}