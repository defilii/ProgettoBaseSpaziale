package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.ForeignKeyIdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    TabellaRepository tabellaRepository;

    TaskRepository taskRepository;
    EntityManager entityManager;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task insert(Task task) {
        if (task.getIdTask() != null && task.getIdTask() > 0) {
            throw new IdMustBeNullException();
        }
        if (task.getTabella() == null || tabellaRepository.findById(task.getTabella().getId()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) {
        if (task.getIdTask() == null || task.getIdTask() == 0) {
            throw new IdMustNotBeNullException();
        }
        if (task.getTabella() == null || tabellaRepository.findById(task.getTabella().getId()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
//        Tabella tabella= task.getTabella();
//        tabella.setLastUpdate(LocalDateTime.now());
//        tabellaRepository.save(tabella);

        return taskRepository.save(task);
    }

    @Override
    public Boolean deleteById(Integer idTask) {
        taskRepository.deleteById(idTask);
        return taskRepository.findById(idTask).isEmpty();
    }

    @Override
    public Task findById(Integer idTask) {
        return taskRepository.findById(idTask).orElse(Task.builder().build());
    }
}
