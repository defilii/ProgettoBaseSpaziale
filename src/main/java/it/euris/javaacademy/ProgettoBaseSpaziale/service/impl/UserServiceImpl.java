package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.UserRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User insert(User user) {
        if(user.getIdUser() != null && user.getIdUser() > 0) {
            throw new IdMustBeNullException();
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if(user.getIdUser() == null || user.getIdUser() == 0) {
            throw new IdMustNotBeNullException();
        }
        return userRepository.save(user);
    }

    @Override
    public Boolean deleteById(Integer idUser) {
        userRepository.deleteById(idUser);
        return userRepository.findById(idUser).isEmpty();
    }

    @Override
    public User findById(Integer idUser) {
        return userRepository.findById(idUser).orElse(User.builder().build());
    }
}
