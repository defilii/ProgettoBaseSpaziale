package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.ChecklistDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CheckmarkDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/checklists")
public class CheckListController {
    ChecklistService checklistService;

    @GetMapping("/v1")
    @Operation(description = """
            This method is used to retrieve all the checklists from the database<br>
            """)
    public List<ChecklistDTO> getAllChecklists() {
        return checklistService.findAll().stream().map(Checklist::toDto).toList();
    }
    @PostMapping("/v1")
    public ChecklistDTO saveChecklist(@RequestBody ChecklistDTO checklistDTO) {
        try{
            Checklist checklist = checklistDTO.toModel();
            return checklistService.insert(checklist).toDto();
        }
        catch(IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/v1")
    public ChecklistDTO updateChecklist(@RequestBody ChecklistDTO checklistDTO) {
        try{
            Checklist checklist = checklistDTO.toModel();
            return checklistService.update(checklist).toDto();
        }
        catch(IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/v1/{id}")
    public Boolean deleteChecklist(@PathVariable("id") Integer idChecklist){
        return checklistService.deleteById(idChecklist);
    }

    @GetMapping("/v1/{id}")
    public ChecklistDTO getChecklistById(@PathVariable("id") Integer idChecklist){
        return checklistService.findById(idChecklist).toDto();
    }
}