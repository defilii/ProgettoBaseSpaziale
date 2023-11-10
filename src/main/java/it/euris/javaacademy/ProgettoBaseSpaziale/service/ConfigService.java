package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Config;

import java.util.List;

public interface ConfigService {
    List<Config> findAll();

    Config insert(Config config);

    Config update(Config config);

    Boolean deleteById(String idConfig);

    Config findById(String idConfig);
}
