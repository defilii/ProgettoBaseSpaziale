package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task insert(Task Task) {
        if(Task.getIdTask() != null && Task.getIdTask() > 0) {
            throw new IdMustBeNullException();
        }
        return taskRepository.save(Task);
    }

    @Override
    public Task update(Task task) {
        if(task.getIdTask() == null || task.getIdTask() == 0) {
            throw new IdMustNotBeNullException();
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
