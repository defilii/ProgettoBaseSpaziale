package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.UserRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.TestUtils;
import org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void shouldReturnAUser(){

        User user = TestUtils.getUser(1);

        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> returnedUsers = userService.findAll();

        assertThat(returnedUsers)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withIntrospectionStrategy(new ComparingSnakeOrCamelCaseFields())
                .isEqualTo(user);
    }

    @Test
    void shouldInsertAUser(){

        User user =  TestUtils.getUser(null);

        when(userRepository.save(any())).thenReturn(user);

        User returnedUser = userService.insert(user);
        assertThat(returnedUser.getUsername())
                .isEqualTo(user.getUsername());
    }

    @Test
    void shouldNotInsertAnyUser(){

        User user = TestUtils.getUser(1);
        lenient().when(userRepository.save(any())).thenReturn(user);

        assertThrows(IdMustBeNullException.class, () -> userService.insert(user));
    }

    @Test
    void shouldUpdateAUser(){

        User user = TestUtils.getUser(1);

        when(userRepository.save(any())).thenReturn(user);

        User returnedUser = userService.update(user);
        assertThat(returnedUser.getUsername())
                .isEqualTo(user.getUsername());
    }

    @Test
    void shouldNotUpdateAnyUser(){

        User user =TestUtils.getUser(null);
        lenient().when(userRepository.save(any())).thenReturn(user);

        assertThatThrownBy(() -> userService.update(user))
                .isInstanceOf(IdMustNotBeNullException.class);
    }

    @Test
    void shouldDeleteAUser() {
        //arrange
        Integer id = 2;

        doNothing().when(userRepository).deleteById(anyInt());
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(userService.deleteById(id));
        Mockito.verify(userRepository, times(1)).deleteById(id);
    }

}