package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.UserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
    TaskService taskService;

    @GetMapping("/v1")
    @Operation(description = """
      This method is used to retrieve all the tasks from the database<br>
      """)
    public List<TaskDTO> getAllTasks() {
        return taskService.findAll().stream().map(Task::toDto).toList();
    }

    @GetMapping("/v1/{id}")
    @Operation(description = """
            This method is used to retrieve one task from the database<br>
            """)
    public TaskDTO getTaskById(@PathVariable("id") Integer idTask) {
        return taskService.findById(idTask).toDto();
    }

    @GetMapping("/v1/priorita/{id}")
    @Operation(description = """
            This method is used to retrieve the priority of a task from the database<br>
            """)
    public String getTaskPriorityById(@PathVariable("id") Integer idTask) {
        String priority = taskService.findById(idTask).toDto().getPriorita();
        return priority == null ? "no priority set" : priority;
    }

    @GetMapping("/v1/expiredate/{id}")
    @Operation(description = """
            This method is used to retrieve the expiration date of a task from the database<br>
            """)
    public String getExpiredatePriorityById(@PathVariable("id") Integer idTask) {
        String expireDate = taskService.findById(idTask).toDto().getDataScadenza();
        return expireDate == null ? "no expire date set" : expireDate;
    }

    @GetMapping("/v1/members/{id}")
    @Operation(description = """
            This method is used to retrieve the members assigned to a task from the database<br>
            """)
    public List<UserDTO> getMembersById(@PathVariable("id") Integer idTask) {
        return taskService.findById(idTask).getUsersTask()
                .stream()
                .map(taskHasUser -> taskHasUser.getUser())
                .map(User::toDto)
                .toList();
    }

    @PostMapping("/v1")
    @Operation(description = """
            This method is used to save one task in the database<br>
            """)
    public TaskDTO saveTask(@RequestBody TaskDTO taskDTO) {
        try {
            Task task = taskDTO.toModel();
            return taskService.insert(task).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/v1")
    @Operation(description = """
            This method is used to update one task in the database<br>
            """)
    public TaskDTO updateTask(@RequestBody TaskDTO taskDTO) {
        try {
            Task task = taskDTO.toModel();
            return taskService.update(task).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/v1/{id}")
    @Operation(description = """
            This method is used to delete one task from the database<br>
            """)
    public Boolean deleteTask(@PathVariable("id") Integer idTask) {
        return taskService.deleteById(idTask);
    }
}
