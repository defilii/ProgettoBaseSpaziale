package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
