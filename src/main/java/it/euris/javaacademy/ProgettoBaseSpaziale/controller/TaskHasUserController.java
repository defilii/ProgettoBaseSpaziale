package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskHasUserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.UserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskHasUserService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/task-has-users")
public class TaskHasUserController {

    TaskHasUserService taskHasUserService;

    @GetMapping("/v1")
    @Operation(description = """
      This method is used to retrieve all the task has users from the database<br>
      """)
    public List<TaskHasUserDTO> getAllTaskUser() {
        return taskHasUserService.findAll().stream().map(TaskHasUser::toDto).toList();
    }
}
