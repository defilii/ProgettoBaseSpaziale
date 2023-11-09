package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class InvalidKeyTokenOrUrl extends Throwable {
    public InvalidKeyTokenOrUrl() {
        super("Response body is empty due to invalid key, token or request");
    }
    public InvalidKeyTokenOrUrl(String message) {
        super(message);
    }
}
