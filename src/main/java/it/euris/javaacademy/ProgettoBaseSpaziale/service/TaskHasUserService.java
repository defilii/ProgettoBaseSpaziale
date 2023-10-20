package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;

import java.util.List;

public interface TaskHasUserService {

    List<TaskHasUser> findAll();


    TaskHasUser insert(TaskHasUser taskHasUser);

    TaskHasUser update(TaskHasUser taskHasUser);

    Boolean deleteById(Integer idTask, Integer idUser);

    TaskHasUser findById(Integer idTask, Integer idUser);
}
