package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

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
        if(taskHasUser.getTaskHasUserKey().getUserId() != null) {
            throw new IdMustBeNullException();
        }
        if(taskHasUser.getTaskHasUserKey().getTaskId() != null) {
            throw new IdMustBeNullException();
        }
        return taskHasUserRepository.save(taskHasUser);
    }

    @Override
    public TaskHasUser update(TaskHasUser taskHasUser) {
        if(taskHasUser.getTaskHasUserKey().getTaskId() == null )  {
            throw new IdMustNotBeNullException();
        }
        if(taskHasUser.getTaskHasUserKey().getUserId() == null )  {
            throw new IdMustNotBeNullException();
        }
        return taskHasUserRepository.save(taskHasUser);
    }

    @Override
    public Boolean deleteById(Integer idTask, Integer idUser) {

        TaskHasUserKey taskHasUserKey = new TaskHasUserKey(idUser,idUser);
        taskHasUserRepository.deleteById(taskHasUserKey);
        return taskHasUserRepository.findById(taskHasUserKey).isEmpty();
    }

    @Override
    public TaskHasUser findById(Integer idTask, Integer idUser) {
        TaskHasUserKey taskHasUserKey= new TaskHasUserKey(idUser, idTask);
        return taskHasUserRepository.findById(taskHasUserKey).orElse(TaskHasUser.builder().build());
    }
}
