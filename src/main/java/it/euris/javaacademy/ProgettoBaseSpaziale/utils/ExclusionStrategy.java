package it.euris.javaacademy.ProgettoBaseSpaziale.utils;

import com.google.gson.FieldAttributes;

public class ExclusionStrategy implements com.google.gson.ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        return field.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

}
