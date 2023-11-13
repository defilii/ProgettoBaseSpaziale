package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.ApiKey;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ApiKeyRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
@Service
@AllArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    ApiKeyRepository apiKeyRepository;

    @Override
    public List<ApiKey> findAll() {
        return apiKeyRepository.findAll();
    }

    @Override
    public ApiKey findMostRecent() {
        return apiKeyRepository.findAll().stream()
                .max(Comparator.comparing(apiKey -> apiKey.getDataInserimento().toEpochSecond(ZoneOffset.ofHours(0))))
                .orElse(ApiKey.builder().build());
    }

    @Override
    public ApiKey insert(ApiKey apiKey) {
        if(apiKey.getIdApikey() != null && apiKey.getIdApikey() > 0) {
            throw new IdMustBeNullException();
        }
        return apiKeyRepository.save(apiKey);
    }

    @Override
    public ApiKey update(ApiKey apiKey) {
        if(apiKey.getIdApikey() == null || apiKey.getIdApikey() == 0) {
            throw new IdMustNotBeNullException();
        }
        return apiKeyRepository.save(apiKey);
    }

    @Override
    public Boolean deleteById(Integer idApiKey) {
        apiKeyRepository.deleteById(idApiKey);
        return apiKeyRepository.findById(idApiKey).isEmpty();
    }

    @Override
    public ApiKey findById(Integer idApiKey) {
        return apiKeyRepository.findById(idApiKey).orElse(ApiKey.builder().build());
    }
}
