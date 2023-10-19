package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.UserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
    TaskService taskService;

    @GetMapping("/getAll")
    @Operation(description = """
      This method is used to retrieve all the tasks from the database<br>
      """)
    public List<TaskDTO> getAllTasks() {
        return taskService.findAll().stream().map(Task::toDto).toList();
    }

    @GetMapping("/getById/{id}")
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

    @GetMapping("/v1/percentage/{id}")
    @Operation(description = """
            This method is used to retrieve the members assigned to a task from the database<br>
            """)
    public String  getPercentageOfCompletedCheckmarks(@PathVariable("id") Integer idTask) {
        List<Checkmark> checkmarksList = taskService.findById(idTask).
                getChecklist()
                .stream()
                .flatMap(checklist -> checklist.getChecklist().stream()).collect(Collectors.toList());

        Float trueCheckmark = (float) checkmarksList.stream()
                .filter(checkmark -> checkmark.getIsItDone().equals(true))
                .count();

        Float total = (float) checkmarksList.size();
        Float percent = (100 * trueCheckmark) / total;
        return percent.isNaN() ? "No checklist to calculate percentage" :
                String.format("%.0f%%",percent);
    }


    @PostMapping("/insert")
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

    @PutMapping("/update")
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

    @PutMapping("/v1/update-description/{id}-{description}")
    @Operation(description = """
            This method is used to update the description of a task from the id<br>
            """)
    public TaskDTO updateDescriptionById(@PathVariable("id") Integer idTask, @PathVariable("description") String newDescription) {
        try {
            Task task = taskService.findById(idTask);
            task.setDescrizione(newDescription);
            return taskService.update(task).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/v1/update-date/{id}-{dayOfMonth}-{month}-{year}")
    @Operation(description = """
             This method is used to update the expire date of a task from the id<br>
            """)
    public TaskDTO updateExpireDateById(@PathVariable("id") Integer idTask
            , @PathVariable("dayOfMonth") Integer dayOfMonth
            , @PathVariable("month") Integer month
            , @PathVariable("year") Integer year
    ) {
        try {
            LocalDateTime date = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
            Task task = taskService.findById(idTask);
            task.setDataScadenza(date);
            return taskService.update(task).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = """
            This method is used to delete one task from the database<br>
            """)
    public Boolean deleteTask(@PathVariable("id") Integer idTask) {
        return taskService.deleteById(idTask);
    }
}
