package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;

import java.util.List;

public interface CheckmarkService {
    List<Checkmark> findAll();

    Checkmark insert(Checkmark checkmark);

    Checkmark update(Checkmark checkmark);

    Boolean deleteById(Integer idCheckmark);

    Checkmark findById(Integer idCheckmark);
}
