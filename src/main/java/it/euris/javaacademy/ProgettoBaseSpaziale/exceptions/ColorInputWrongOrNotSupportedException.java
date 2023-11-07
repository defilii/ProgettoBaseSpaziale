package it.euris.javaacademy.ProgettoBaseSpaziale.exceptions;

public class ColorInputWrongOrNotSupportedException extends Throwable {
    public ColorInputWrongOrNotSupportedException() {
        super("Color isnt supported or mistyped");
    }
    public ColorInputWrongOrNotSupportedException(String message) {
        super(message);
    }
}
