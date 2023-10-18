package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;

import java.util.List;

public interface CommentoService {
    List<Commento> findAll();

    Commento insert(Commento commento);

    Commento update(Commento commento);

    Boolean deleteById(Integer idCommento);

    Commento findById(Integer idCommento);
}
