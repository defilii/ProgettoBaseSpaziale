package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    ChecklistRepository checklistRepository;
    @Override
    public List<Checklist> findAll() {
        return checklistRepository.findAll();
    }
    @Override
    public Checklist insert(Checklist Checklist) {
        if(Checklist.getIdChecklist() != null && Checklist.getIdChecklist() > 0) {
            throw new IdMustBeNullException();
        }
        return checklistRepository.save(Checklist);
    }

    @Override
    public Checklist update(Checklist Checklist) {
        if(Checklist.getIdChecklist() == null || Checklist.getIdChecklist() == 0) {
            throw new IdMustNotBeNullException();
        }
        return checklistRepository.save(Checklist);
    }

    @Override
    public Boolean deleteById(Integer idChecklist) {
        checklistRepository.deleteById(idChecklist);
        return checklistRepository.findById(idChecklist).isEmpty();
    }

    @Override
    public Checklist findById(Integer idChecklist) {
        return checklistRepository.findById(idChecklist).orElse(Checklist.builder().build());
    }
}
