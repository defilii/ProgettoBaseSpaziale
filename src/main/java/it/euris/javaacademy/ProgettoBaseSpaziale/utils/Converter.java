package it.euris.javaacademy.ProgettoBaseSpaziale.utils;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;

import java.time.LocalDateTime;

public class Converter {

    public static LocalDateTime stringToLocalDateTime(String value){
        return value == null ? null : LocalDateTime.parse(value);
    }

    public static String localDateTimeToString(LocalDateTime value){
        return value == null ? null : value.toString();
    }

    public static Priorita stringToPriorita(String value){
        for(Priorita valorePriorita : Priorita.values()){
            if (valorePriorita.name().equalsIgnoreCase(value))
                return valorePriorita;
        }
        return null;
    }

    public static String prioritaToString(Priorita value){
        return  value== null? null : value.name();
    }

    public static String numberToString(Number value){
        return value == null ? null : value.toString();
    }
    public static Integer stringToInteger(String value){
        return value == null ? null : Integer.parseInt(value);
    }





}
