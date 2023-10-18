package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class TabellaServiceImpl implements TabellaService {
    TabellaRepository tabellaRepository;
    @Override
    public List<Tabella> findAll() {
        return null;
    }

    @Override
    public Tabella insert(Tabella tabella) {
        return null;
    }

    @Override
    public Tabella update(Tabella tabella) {
        return null;
    }

    @Override
    public Boolean deleteById(Integer idTabella) {
        return null;
    }

    @Override
    public Tabella findById(Integer idTabella) {
        return null;
    }
}
