package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;
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
        return null;
    }

    @Override
    public TaskHasUser insert(TaskHasUser taskHasUser) {
        return null;
    }

    @Override
    public TaskHasUser update(TaskHasUser taskHasUser) {
        return null;
    }

    @Override
    public Boolean deleteById(TaskHasUserKey idTaskHasUser) {
        return null;
    }

    @Override
    public TaskHasUser findById(TaskHasUserKey idTaskHasUser) {
        return null;
    }
}
