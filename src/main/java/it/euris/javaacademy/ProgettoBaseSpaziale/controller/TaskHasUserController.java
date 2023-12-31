package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskHasUserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskHasUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/task-has-users")

public class TaskHasUserController {

    TaskHasUserService taskHasUserService;

    @GetMapping("/getAll")
    @Operation(description = """
      This method is used to retrieve all the task has TaskHasUsers from the database<br>
      """)
    public List<TaskHasUserDTO> getAllTaskTaskHasUser() {
        return taskHasUserService.findAll().stream().map(TaskHasUser::toDto).toList();
    }

    @GetMapping("/getById/{id-task}-{id-user}")
    @Operation(description = """
            This method is used to retrieve one TaskHasUser from the database<br>
            """)
    public TaskHasUserDTO getTaskHasUserById(@PathVariable("id-task") Integer idTask, @PathVariable("id-user") Integer idUser) {
        return taskHasUserService.findById(idTask,idUser).toDto();
    }

    @PostMapping("/insert")
    @Operation(description = """
            This method is used to save one TaskHasUser in the database<br>
            """)
    public TaskHasUserDTO saveTaskHasUser(@RequestBody TaskHasUserDTO TaskHasUserDTO) {
        try {
            TaskHasUser TaskHasUser = TaskHasUserDTO.toModel();
            return taskHasUserService.insert(TaskHasUser).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update")
    @Operation(description = """
            This method is used to update one TaskHasUser in the database<br>
            """)
    public TaskHasUserDTO updateTaskHasUser(@RequestBody TaskHasUserDTO taskHasUserDTO) {
        try {
            TaskHasUser taskHasUser = taskHasUserDTO.toModel();
            return taskHasUserService.update(taskHasUser).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

   @DeleteMapping("/delete/{id-task}-{id-user}")
   @Operation(description = """
           This method is used to delete one TaskHasUser from the database<br>
           """)
   public Boolean deleteTaskHasUser(@PathVariable("id-task") Integer idTask, @PathVariable("id-user") Integer idUser) {
        return taskHasUserService.deleteById(idTask,idUser);
    }
}

