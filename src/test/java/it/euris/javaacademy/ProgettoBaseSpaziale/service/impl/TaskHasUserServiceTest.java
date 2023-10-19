package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskHasUserRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskHasUserServiceTest {
    @Mock
    TaskHasUserRepository taskHasUserRepository;

    @InjectMocks
    TaskHasUserServiceImpl taskHasUserService;


    @Test
    void shouldReturnATaskHasUser() {
        TaskHasUser taskHasUser = TestUtils.getTaskHasUserId(1, 1);

        List<TaskHasUser> taskHasUsers = List.of(taskHasUser);

        when(taskHasUserRepository.findAll()).thenReturn(taskHasUsers);

        List<TaskHasUser> returnedTaskHasUsers = taskHasUserService.findAll();

        assertThat(returnedTaskHasUsers)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withIntrospectionStrategy(new ComparingSnakeOrCamelCaseFields())
                .isEqualTo(taskHasUser);
    }

    @Test
    void shouldInsertATaskHasUser() {

        TaskHasUser taskHasUser = TestUtils.getTaskHasUserId(null, null);

        when(taskHasUserRepository.save(any())).thenReturn(taskHasUser);

        TaskHasUser returnedTaskHasUser = taskHasUserService.insert(taskHasUser);
        assertThat(returnedTaskHasUser.getTask())
                .isEqualTo(taskHasUser.getTask());
    }

    @Test
    void shouldNotInsertAnyTaskHasUser() {

        TaskHasUser taskHasUser = TestUtils.getTaskHasUserId(1, 1);
        lenient().when(taskHasUserRepository.save(any())).thenReturn(taskHasUser);

        assertThrows(IdMustBeNullException.class, () -> taskHasUserService.insert(taskHasUser));

    }

    @Test
    void shouldUpdateATaskHasUser() {

        TaskHasUser taskHasUser = TestUtils.getTaskHasUserId(1, 1);

        when(taskHasUserRepository.save(any())).thenReturn(taskHasUser);

        TaskHasUser returnedTaskHasUser = taskHasUserService.update(taskHasUser);
        assertThat(returnedTaskHasUser.getTask())
                .isEqualTo(taskHasUser.getTask());
    }

    @Test
    void shouldNotUpdateAnyTaskHasUser() {

        TaskHasUser taskHasUser = TestUtils.getTaskHasUserId(null, null);
        lenient().when(taskHasUserRepository.save(any())).thenReturn(taskHasUser);

        assertThatThrownBy(() -> taskHasUserService.update(taskHasUser))
                .isInstanceOf(IdMustNotBeNullException.class);
    }

 @Test
    void shouldDeleteATaskHasUser() {//TODO
        //arrange
        Integer idTask = 1;
        Integer idUser = 1;
    //   TaskHasUser taskHasUser = TestUtils.getTaskHasUserId(idTask,idUser);
       TaskHasUserKey idKey= new TaskHasUserKey(idUser,idTask);

        doNothing().when(taskHasUserRepository).deleteById(any());
        when(taskHasUserRepository.findById(idKey)).thenReturn(Optional.empty());
        assertTrue(taskHasUserService.deleteById(idTask, idUser));
        Mockito.verify(taskHasUserRepository, times(1)).deleteById(idKey);
    }
}