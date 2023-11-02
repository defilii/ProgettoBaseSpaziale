package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;

import java.util.List;

public interface PriorityService {

    List<Priority> findAll();

    Priority insert(Priority priority);

    Priority update(Priority priority);

    Boolean deleteById(Integer idPriority);

    Priority findById(Integer idPriority);

    Priority findByName(String name);

}
