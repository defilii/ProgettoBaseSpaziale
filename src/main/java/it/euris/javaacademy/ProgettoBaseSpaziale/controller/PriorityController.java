package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.PriorityDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.PriorityService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NameNotFoundException;
import java.util.List;

@AllArgsConstructor
@RestController
@ToString
@RequestMapping("/priorities")
public class PriorityController {
    PriorityService priorityService;
    TaskService taskService;

    @GetMapping("/get-all-priorities")
    @Operation(description = """
            This method is used to retrieve all the priorities from the database<br>
            """)
    public List<PriorityDTO> getAllPriorities() {
        return priorityService.findAll().stream().map(Priority::toDto).toList();
    }

    @PostMapping("/insert")
    @Operation(description = """
            This method is used to insert a priority to the database<br>
            """)
    public PriorityDTO savePriority(@RequestBody PriorityDTO priorityDTO) {
        try {
            Priority priority = priorityDTO.toModel();
            return priorityService.insert(priority).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    @PutMapping("/update")
    @Operation(description = """
            This method is used to update a priority to the database<br>
            """)
    public PriorityDTO updatePriority(@RequestBody PriorityDTO priorityDTO) {
        try {
            Priority priority = priorityDTO.toModel();
            return priorityService.update(priority).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = """
            This method is used to delete a priority by id from the database<br>
            """)
    public Boolean deletePriority(@PathVariable("id") Integer idPriority) {
        return priorityService.deleteById(idPriority);
    }

    @GetMapping("/get-by-id/{id}")
    @Operation(description = """
            This method is used to get a priority by id from the database<br>
            """)
    public PriorityDTO getPriorityById(@PathVariable("id") Integer idPriority) {
        return priorityService.findById(idPriority).toDto();
    }

    @GetMapping("/get-by-name/{name}")
    public PriorityDTO getPriorityByName(@PathVariable("name") String name) throws NameNotFoundException {
        return priorityService.findByName(name).toDto();
    }

    @PutMapping("/add-existing-priority-to-task/{name}-{id-task}")
    @Operation(description = """
            This method is used to add a priority to a task
            """)
    public TaskDTO addExistingPriorityToTask(@PathVariable("name") String name, @PathVariable("id-task") Integer idTask) {
        Task task = taskService.findById(idTask);
        Priority priority = priorityService.findByName(name);
        priority.addTask(task);
        task.addPriority(priority);
        taskService.findById(idTask).toDto().setPriorita(priority.getName());
        priorityService.update(priority).toDto();

        return taskService.update(task).toDto();
    }

    @PostMapping("/insert-new-priority-add-to-task/{id-task}")
    @Operation(description = """
            This method is used to add a priority to a task
            """)
    public TaskDTO insertNewPriorityAddToTask( @PathVariable("id-task") Integer idTask, @RequestBody PriorityDTO priorityDto) {
        Task task = taskService.findById(idTask);
        try {
            Priority priority = priorityDto.toModel();
            priority.addTask(task);
            task.addPriority(priority);
            taskService.findById(idTask).toDto().setPriorita(priority.getName());
            priorityService.insert(priority).toDto();
        }catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage()+(" in priority"));
        }
        return taskService.update(task).toDto();
    }
}
