package it.euris.javaacademy.ProgettoBaseSpaziale.utils;

import java.time.LocalDateTime;

public class Converter {

    public static LocalDateTime stringToLocalDateTime(String value){
        return value == null ? null : LocalDateTime.parse(value);
    }

    public static String localDateTimeToString(LocalDateTime value){
        return value == null ? null : value.toString();
    }

}
