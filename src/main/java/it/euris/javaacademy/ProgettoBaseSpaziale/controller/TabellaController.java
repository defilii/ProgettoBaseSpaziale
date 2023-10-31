package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TabellaDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.pre_insert.TabellaInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@RestController
@Data
@RequestMapping("/tabelle")
public class TabellaController {
    TabellaService tabellaService;

    @GetMapping("/get-all")
    @Operation(description = """
            This method is used to retrieve all the tables from the database<br>
            """)
    public List<TabellaDTO> getAllTabelle() {
        return tabellaService.findAll().stream().map(Tabella::toDto).toList();
    }

    @PostMapping("/insert")
    @Operation(description = """
            This method is used to insert a table to the database<br>
            """)
    public TabellaInsert saveTabella(@RequestBody TabellaInsert tabellaInsert) {
        try {
            Tabella tabella = tabellaInsert.toModel();
            return tabellaService.insert(tabella).toPreInsert();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update")
    @Operation(description = """
            This method is used to update a table from the database<br>
            """)
    public TabellaInsert updateTabella(@RequestBody TabellaInsert tabellaInsert) {
        try {
            Tabella tabella = tabellaInsert.toModel();
            return tabellaService.update(tabella).toPreInsert();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(description = """
            This method is used to delete a table by id from the database<br>
            """)
    public Boolean deleteTabella(@PathVariable("id") Integer idTabella) {
        return tabellaService.deleteById(idTabella);
    }

    @GetMapping("/get-by-id/{id}")
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
                .filter(task -> Priorita.ALTA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/medium-priority-tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the medium priority tasks from a table id<br>
            """)
    public List<TaskDTO> getAllMediumPriorityTasksByTabellaId(@PathVariable("id") Integer idTabella) {

        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> Priorita.MEDIA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/desired-priority-tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the desired priority tasks from a table id<br>
            """)
    public List<TaskDTO> getAllDesiredPriorityTasksByTabellaId(@PathVariable("id") Integer idTabella) {

        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> Priorita.DESIDERATA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/low-priority-tasks/{id}")
    @Operation(description = """
            This method is used to retrieve all the low  priority tasks from a table id<br>
            """)
    public List<TaskDTO> getAllLowPriorityTasksByTabellaId(@PathVariable("id") Integer idTabella) {

        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> Priorita.BASSA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/high-priority-tasks")
    @Operation(description = """
            This method is used to retrieve all the high priority tasks from the database <br>
            """)
    public List<TaskDTO> getAllHighPriorityTasks() {

        return tabellaService.findAll().
                stream()
                .map(tabella -> tabella.getTasks())
                .flatMap(Collection::stream)
                .filter(task -> Priorita.ALTA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/medium-priority-tasks")
    @Operation(description = """
            This method is used to retrieve all the medium priority tasks from the database <br>
            """)
    public List<TaskDTO> getAllMediumPriorityTasks() {

        return tabellaService.findAll().
                stream()
                .map(tabella -> tabella.getTasks())
                .flatMap(Collection::stream)
                .filter(task -> Priorita.MEDIA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/desired-priority-tasks")
    @Operation(description = """
            This method is used to retrieve all the desired priority tasks from the database <br>
            """)
    public List<TaskDTO> getAllDesiredPriorityTasks() {

        return tabellaService.findAll().
                stream()
                .map(tabella -> tabella.getTasks())
                .flatMap(Collection::stream)
                .filter(task -> Priorita.DESIDERATA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/low-priority-tasks")
    @Operation(description = """
            This method is used to retrieve all the low  priority tasks from the database <br>
            """)
    public List<TaskDTO> getAllLowPriorityTasks() {

        return tabellaService.findAll().
                stream()
                .map(tabella -> tabella.getTasks())
                .flatMap(Collection::stream)
                .filter(task -> Priorita.BASSA.equals(task.getPriorita()))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/expire-in-{days}")
    @Operation(description = """
            This method is used to retrieve all the tasks about to expire from all tables from the database<br>
            """)
    public List<TaskDTO> getAllTasksABoutToExpireIn(@PathVariable("days") Integer days) {
        return tabellaService.findAll().stream()
                .map(Tabella::getTasks)
                .flatMap(Collection::stream)
                .filter(task -> null != task.getDataScadenza())
                .filter(task -> task.getDataScadenza().isBefore(LocalDateTime.now().plusDays(days)))
                .map(Task::toDto).toList();
    }

    @GetMapping("/v1/expire-in-{days}/{id}")
    @Operation(description = """
            This method is used to retrieve all the tasks about to expire from all tables from the database<br>
            """)
    public List<TaskDTO> getAllTasksABoutToExpireByIdTabella(@PathVariable("days") Integer days, @PathVariable("id") Integer idTabella) {
        return tabellaService.findById(idTabella).getTasks().stream()
                .filter(task -> null != task.getDataScadenza())
                .filter(task -> task.getDataScadenza().isBefore(LocalDateTime.now().plusDays(days)))
                .map(Task::toDto).toList();
    }
}