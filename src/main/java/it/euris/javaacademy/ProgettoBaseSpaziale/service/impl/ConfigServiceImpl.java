package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Config;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ConfigRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    ConfigRepository configRepository;

    @Override
    public List<Config> findAll() {
        return configRepository.findAll();
    }

    @Override
    public Config insert(Config config) {
        if (config.getAction() != null) {
            throw new IdMustBeNullException();
        }
        return configRepository.save(config);
    }

    @Override
    public Config update(Config config) {
        if (config.getAction() == null) {
            throw new IdMustNotBeNullException();
        }
        return configRepository.save(config);
    }

    @Override
    public Boolean deleteById(String idConfig) {
        configRepository.deleteById(idConfig);
        return configRepository.findById(idConfig).isEmpty();
    }

    @Override
    public Config findById(String idConfig) {
        return configRepository.findById(idConfig).orElse(Config.builder().build());
    }
}
