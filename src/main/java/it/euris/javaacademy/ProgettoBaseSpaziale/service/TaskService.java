package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAll();


    Task insert(Task task);

    Task update(Task task);

    Boolean deleteById(Integer idTask);

    Task findById(Integer idTask);
}
