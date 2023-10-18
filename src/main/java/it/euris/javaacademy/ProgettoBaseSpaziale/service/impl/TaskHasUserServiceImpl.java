package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskHasUserRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskHasUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class TaskHasUserServiceImpl implements TaskHasUserService {
    TaskHasUserRepository taskHasUserRepository;
    @Override
    public List<TaskHasUser> findAll() {
        return taskHasUserRepository.findAll();
    }

    @Override
    public TaskHasUser insert(TaskHasUser taskHasUser) {
        if(taskHasUser.getTaskHasUserKey() != null ) {
            throw new IdMustBeNullException();
        }
        return taskHasUserRepository.save(taskHasUser);
    }

    @Override
    public TaskHasUser update(TaskHasUser taskHasUser) {
        if(taskHasUser.getTaskHasUserKey() == null )  {
            throw new IdMustNotBeNullException();
        }
        return taskHasUserRepository.save(taskHasUser);
    }

    @Override
    public Boolean deleteById(TaskHasUserKey idTask) {
        taskHasUserRepository.deleteById(idTask);
        return taskHasUserRepository.findById(idTask).isEmpty();
    }

    @Override
    public TaskHasUser findById(TaskHasUserKey idTask) {
        return taskHasUserRepository.findById(idTask).orElse(TaskHasUser.builder().build());
    }
}
