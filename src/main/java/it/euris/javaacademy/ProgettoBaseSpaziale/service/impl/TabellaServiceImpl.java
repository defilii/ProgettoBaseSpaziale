package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TabellaServiceImpl implements TabellaService {
    TabellaRepository tabellaRepository;

    @Override
    public List<Tabella> findAll() {
        return tabellaRepository.findAll();
    }

    @Override
    public Tabella insert(Tabella tabella) {
        if (tabella.getId() != null && tabella.getId() > 0) {
            throw new IdMustBeNullException();
        }
        tabella.setLastUpdate(LocalDateTime.now());
        return tabellaRepository.save(tabella);
    }

    @Override
    public Tabella update(Tabella tabella) {
        if (tabella.getId() == null || tabella.getId() == 0) {
            throw new IdMustNotBeNullException();
        }
        tabella.setLastUpdate(LocalDateTime.now());
        return tabellaRepository.save(tabella);
    }

    @Override
    public Boolean deleteById(Integer idTabella) {
        tabellaRepository.deleteById(idTabella);
        return tabellaRepository.findById(idTabella).isEmpty();
    }

    @Override
    public Tabella findById(Integer idTabella) {
        return tabellaRepository.findById(idTabella).orElse(Tabella.builder().build());
    }
}
