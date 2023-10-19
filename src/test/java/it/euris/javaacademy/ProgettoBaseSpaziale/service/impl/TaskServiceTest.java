package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
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
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    void shouldReturnATask(){

        Task task = TestUtils.getTask(1);

        List<Task> tasks = List.of(task);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> returnedTasks = taskService.findAll();

        assertThat(returnedTasks)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withIntrospectionStrategy(new ComparingSnakeOrCamelCaseFields())
                .isEqualTo(task);
    }

    @Test
    void shouldInsertATask(){

        Task task = TestUtils.getTask(null);

        when(taskRepository.save(any())).thenReturn(task);

        Task returnedTask = taskService.insert(task);
        assertThat(returnedTask.getTaskName())
                .isEqualTo(task.getTaskName());
    }

    @Test
    void shouldNotInsertAnyTask(){

        Task task = TestUtils.getTask(1);
        lenient().when(taskRepository.save(any())).thenReturn(task);

        assertThrows(IdMustBeNullException.class, () -> taskService.insert(task));
    }

    @Test
    void shouldUpdateATask(){

        Task task = TestUtils.getTask(1);

        when(taskRepository.save(any())).thenReturn(task);

        Task returnedTask = taskService.update(task);
        assertThat(returnedTask.getTaskName())
                .isEqualTo(task.getTaskName());
    }

    @Test
    void shouldNotUpdateAnyTask(){

        Task task = TestUtils.getTask(null);
        lenient().when(taskRepository.save(any())).thenReturn(task);

        assertThatThrownBy(() -> taskService.update(task))
                .isInstanceOf(IdMustNotBeNullException.class);
    }

    @Test
    void shouldDeleteATask() {
        //arrange
        Integer id = 3;

        doNothing().when(taskRepository).deleteById(anyInt());
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(taskService.deleteById(id));
        Mockito.verify(taskRepository, times(1)).deleteById(id);
    }
}