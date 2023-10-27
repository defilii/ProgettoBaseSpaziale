package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class BoardIdMissingException extends  RuntimeException{
    public BoardIdMissingException() {
        super("Board id is null, execute a synchronization from trello first");
    }
    public BoardIdMissingException(String message) {
        super(message);
    }
}