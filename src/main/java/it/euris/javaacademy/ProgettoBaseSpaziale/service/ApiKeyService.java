package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.ApiKey;

import java.util.List;

public interface ApiKeyService {
    List<ApiKey> findAll();
    ApiKey findMostRecent();

    ApiKey insert(ApiKey apiKey);

    ApiKey update(ApiKey apiKey);

    Boolean deleteById(Integer idApiKey);

    ApiKey findById(Integer idApiKey);
}
