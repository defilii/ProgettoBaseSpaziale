package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.ForeignKeyIdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CheckmarkRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CheckmarkServiceImpl implements CheckmarkService {
    CheckmarkRepository checkmarkRepository;
    ChecklistRepository checklistRepository;

    @Override
    public List<Checkmark> findAll() {
        return checkmarkRepository.findAll();
    }

    @Override
    public Checkmark insert(Checkmark checkmark) {
        if (checkmark.getIdCheckmark() != null && checkmark.getIdCheckmark() > 0) {
            throw new IdMustBeNullException();
        }
        if (checklistRepository.findById(checkmark.getChecklist().getIdChecklist()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        return checkmarkRepository.save(checkmark);
    }

    @Override
    public Checkmark update(Checkmark checkmark) {
        if (checkmark.getIdCheckmark() == null || checkmark.getIdCheckmark() == 0) {
            throw new IdMustNotBeNullException();
        }
        if (checklistRepository.findById(checkmark.getChecklist().getIdChecklist()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        return checkmarkRepository.save(checkmark);
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
