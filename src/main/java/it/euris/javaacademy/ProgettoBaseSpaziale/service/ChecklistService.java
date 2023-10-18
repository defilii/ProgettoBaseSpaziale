package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;

import java.util.List;

public interface ChecklistService {
    List<Checklist> findAll();

    Checklist insert(Checklist checklist);

    Checklist update(Checklist checklist);

    Boolean deleteById(Integer idChecklist);

    Checklist findById(Integer idChecklist);
}
