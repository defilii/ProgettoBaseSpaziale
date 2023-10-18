package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;

import java.util.List;

public interface TaskHasUserService {

    List<TaskHasUser> findAll();


    TaskHasUser insert(TaskHasUser taskHasUser);

    TaskHasUser update(TaskHasUser taskHasUser);

    Boolean deleteById(TaskHasUserKey idTaskHasUser);

    TaskHasUser findById(TaskHasUserKey idTaskHasUser);
}