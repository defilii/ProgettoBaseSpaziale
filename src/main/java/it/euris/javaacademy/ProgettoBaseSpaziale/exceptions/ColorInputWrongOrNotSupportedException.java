package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class ColorInputWrongOrNotSupportedException extends Throwable {
    public ColorInputWrongOrNotSupportedException() {
        super("Color isnt supported or misstyped");
    }
    public ColorInputWrongOrNotSupportedException(String message) {
        super(message);
    }
}
