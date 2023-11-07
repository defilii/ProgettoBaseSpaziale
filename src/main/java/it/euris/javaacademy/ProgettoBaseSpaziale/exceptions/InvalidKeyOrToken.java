package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class InvalidKeyOrToken extends Throwable {
    public InvalidKeyOrToken() {
        super("Response body is empty due to invalid key or token");
    }
    public InvalidKeyOrToken(String message) {
        super(message);
    }
}
