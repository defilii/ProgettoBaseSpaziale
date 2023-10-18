package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CheckmarkRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class CheckmarkServiceImpl implements CheckmarkService {
    CheckmarkRepository checkmarkRepository;
    @Override
    public List<Checkmark> findAll() {
        return null;
    }

    @Override
    public Checkmark insert(Checkmark checkmark) {
        return null;
    }

    @Override
    public Checkmark update(Checkmark checkmark) {
        return null;
    }

    @Override
    public Boolean deleteById(Integer idCheckmark) {
        return null;
    }

    @Override
    public Checkmark findById(Integer idCheckmark) {
        return null;
    }
}
