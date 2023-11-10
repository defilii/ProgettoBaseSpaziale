package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class RejectWebTrafficExceeded extends Throwable {
    public RejectWebTrafficExceeded() {
        super("Too much traffic or api calls limit reached, try later");
    }
    public RejectWebTrafficExceeded(String message) {
        super(message);
    }
}
