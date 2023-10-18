package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;

import java.util.List;

public interface TabellaService {
    List<Tabella> findAll();

    Tabella insert(Tabella tabella);

    Tabella update(Tabella tabella);

    Boolean deleteById(Integer idTabella);

    Tabella findById(Integer idTabella);
}
