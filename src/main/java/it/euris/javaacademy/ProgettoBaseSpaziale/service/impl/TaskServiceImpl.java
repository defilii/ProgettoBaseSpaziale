package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.ForeignKeyIdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    TabellaRepository tabellaRepository;

    TaskRepository taskRepository;

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task insert(Task task) {
        if (task.getIdTask() != null && task.getIdTask() > 0) {
            throw new IdMustBeNullException();
        }
        if (tabellaRepository.findById(task.getTabella().getId()).isEmpty()) {
            throw new ForeignKeyIdMustNotBeNullException();
        }
        return taskRepository.save(task);
    }

    @Override
    public Task update(Task task) {
        if (task.getIdTask() == null || task.getIdTask() == 0) {
            throw new IdMustNotBeNullException();
        }
        if (tabellaRepository.findById(task.getTabella().getId()).isEmpty()){
            throw new ForeignKeyIdMustNotBeNullException();
        }
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
