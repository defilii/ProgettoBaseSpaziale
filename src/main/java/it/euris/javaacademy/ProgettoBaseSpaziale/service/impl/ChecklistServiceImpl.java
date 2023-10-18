package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CheckmarkRepository;
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
        return null;
    }

    @Override
    public Checklist insert(Checklist checklist) {
        return null;
    }

    @Override
    public Checklist update(Checklist checklist) {
        return null;
    }

    @Override
    public Boolean deleteById(Integer idChecklist) {
        return null;
    }

    @Override
    public Checklist findById(Integer idChecklist) {
        return null;
    }
}
