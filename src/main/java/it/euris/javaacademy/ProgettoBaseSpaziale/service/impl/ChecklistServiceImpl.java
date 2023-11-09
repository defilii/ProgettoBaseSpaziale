package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.ForeignKeyIdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    ChecklistRepository checklistRepository;
    TaskRepository taskRepository;
    @Override
    public List<Checklist> findAll() {
        return checklistRepository.findAll();
    }
    @Override
    public Checklist insert(Checklist checklist) {
        if(checklist.getIdChecklist() != null && checklist.getIdChecklist() > 0) {
            throw new IdMustBeNullException();
        }     if (checklist.getTask() == null ||taskRepository.findById(checklist.getTask().getIdTask()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        return checklistRepository.save(checklist);
    }

    @Override
    public Checklist update(Checklist checklist) {
        if(checklist.getIdChecklist() == null || checklist.getIdChecklist() == 0) {
            throw new IdMustNotBeNullException();
        }     if (checklist.getTask() == null ||taskRepository.findById(checklist.getTask().getIdTask()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        Optional<Checklist> checklist1 = checklistRepository.findById(checklist.getIdChecklist());
        if (checklist1.isPresent()) {
            Task task = checklist1.get().getTask();
            task.setLastUpdate(LocalDateTime.now());
            taskRepository.save(task);
        }
        return checklistRepository.save(checklist);
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
