package it.euris.javaacademy.ProgettoBaseSpaziale.service;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User insert(User user);

    User update(User user);

    Boolean deleteById(Integer idUser);

    User findById(Integer idUser);
}
