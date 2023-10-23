package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class IdAndForeignKeyMustNotBeNullException extends  RuntimeException{

    public IdAndForeignKeyMustNotBeNullException() {
        super("Id and foreign key must not be null.");
    }
    public IdAndForeignKeyMustNotBeNullException(String message) {
        super(message);
    }
}
