package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.NameNotFoundException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.PriorityRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.PriorityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PriorityServiceImpl implements PriorityService {

    PriorityRepository priorityRepository;

    @Override
    public List<Priority> findAll() {
        return priorityRepository.findAll();
    }

    @Override
    public Priority insert(Priority priority) {
        if (priority.getId() != null && priority.getId() > 0) {
            throw new IdMustBeNullException();
        }
        return priorityRepository.save(priority);
    }

    @Override
    public Priority update(Priority priority) {
        if (priority.getId() == null || priority.getId() == 0) {
            throw new IdMustNotBeNullException();
        }
        return priorityRepository.save(priority);
    }

    @Override
    public Boolean deleteById(Integer idPriority) {
        priorityRepository.deleteById(idPriority);
        return priorityRepository.findById(idPriority).isEmpty();
    }

    @Override
    public Priority findById(Integer idPriority) {
        return priorityRepository.findById(idPriority).orElse(Priority.builder().build());
    }

    @Override
    public Priority findByName(String name) {

        if (priorityRepository.findByNameIgnoreCase(name.trim()) == null) {
            throw new NameNotFoundException();
        }
        return priorityRepository.findByNameIgnoreCase(name.trim());
    }
}
