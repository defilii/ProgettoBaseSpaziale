package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TabellaDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@Data
@RequestMapping("/tabelle")
public class TabellaController {
    TabellaService tabellaService;

    @GetMapping("/v1")
    @Operation(description = """
            This method is used to retrieve all the tables from the database<br>
            """)
    public List<TabellaDTO> getAllTabelle() {
        return tabellaService.findAll().stream().map(Tabella::toDto).toList();
    }

    @PostMapping("/v1")
    @Operation(description = """
            This method is used to insert a table to the database<br>
            """)
    public TabellaDTO saveTabella(@RequestBody TabellaDTO tabellaDTO) {
        try {
            Tabella tabella = tabellaDTO.toModel();
            return tabellaService.insert(tabella).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/v1")
    @Operation(description = """
            This method is used to update a table from the database<br>
            """)
    public TabellaDTO updateTabella(@RequestBody TabellaDTO tabellaDTO) {
        try {
            Tabella tabella = tabellaDTO.toModel();
            return tabellaService.update(tabella).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/v1/{id}")
    @Operation(description = """
            This method is used to delete a table by id from the database<br>
            """)
    public Boolean deleteTabella(@PathVariable("id") Integer idTabella) {
        return tabellaService.deleteById(idTabella);
    }

    @GetMapping("/v1/{id}")
    @Operation(description = """
            This method is used to get a table by id from the database<br>
            """)
    public TabellaDTO getTabellaById(@PathVariable("id") Integer idTabella) {
        return tabellaService.findById(idTabella).toDto();
    }

    @GetMapping("/v1/tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the tasks from a table id<br>
            """)
    public List<TaskDTO> getTasksByTabellaId(@PathVariable("id") Integer idTabella) {
        return tabellaService.findById(idTabella).getTasks().stream().map(Task::toDto).toList();
    }

   @GetMapping("/v1/high-priority-tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the high priority tasks from a table id<br>
            """)
    public List<TaskDTO> getAllHighPriorityTasksByTabellaId(@PathVariable("id") Integer idTabella) {

        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> task.getPriorita().equals(Priorita.ALTA))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/medium-priority-tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the medium priority tasks from a table id<br>
            """)
    public List<TaskDTO> getAllMediumPriorityTasksByTabellaId(@PathVariable("id") Integer idTabella) {

        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> task.getPriorita().equals(Priorita.MEDIA))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/desired-priority-tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the medium priority tasks from a table id<br>
            """)
    public List<TaskDTO> getAllDesiredPriorityTasksByTabellaId(@PathVariable("id") Integer idTabella) {

        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> task.getPriorita().equals(Priorita.DESIDERATA))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/low-priority-tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the low  priority tasks from a table id<br>
            """)
    public List<TaskDTO> getAllLowPriorityTasksByTabellaId(@PathVariable("id") Integer idTabella) {

        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> task.getPriorita().equals(Priorita.BASSA))
                .map(Task::toDto).toList();
    }


/*    @GetMapping("/v1/expire-in-{id}")
    @Operation(description = """
         This method is used to retrieve all the tasks about to expire from all tables from the database<br>
         """)
    public List<TaskDTO> getAllTasksABoutToExpireIn(@PathVariable("id") Integer days) {
        return tabellaService.findAll().stream()
                .map(Tabella::getTasks)
                .flatMap(tasks -> tasks.stream())
                .filter(task -> task.getDataScadenza().isAfter(LocalDateTime.now().minusDays(days)))
                .map(Task::toDto).toList();
    }*/

}