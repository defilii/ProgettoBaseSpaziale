package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
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
        return checkmarkRepository.findAll();
    }

    @Override
    public Checkmark insert(Checkmark Checkmark) {
        if(Checkmark.getIdCheckmark() != null && Checkmark.getIdCheckmark() > 0) {
            throw new IdMustBeNullException();
        }
        return checkmarkRepository.save(Checkmark);
    }

    @Override
    public Checkmark update(Checkmark Checkmark) {
        if(Checkmark.getIdCheckmark() == null || Checkmark.getIdCheckmark() == 0) {
            throw new IdMustNotBeNullException();
        }
        return checkmarkRepository.save(Checkmark);
    }

    @Override
    public Boolean deleteById(Integer idCheckmark) {
        checkmarkRepository.deleteById(idCheckmark);
        return checkmarkRepository.findById(idCheckmark).isEmpty();
    }

    @Override
    public Checkmark findById(Integer idCheckmark) {
        return checkmarkRepository.findById(idCheckmark).orElse(Checkmark.builder().build());
    }
}
