package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.ChecklistDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/checklists")
public class ChecklistController {
    ChecklistService checklistService;

    @GetMapping("/getAll")
    @Operation(description = """
            This method is used to retrieve all the checklists from the database<br>
            """)
    public List<ChecklistDTO> getAllChecklists() {
        return checklistService.findAll().stream().map(Checklist::toDto).toList();
    }

    @PostMapping("/insert")
    public ChecklistDTO saveChecklist(@RequestBody ChecklistDTO checklistDTO) {
        try {
            Checklist checklist = checklistDTO.toModel();
            return checklistService.insert(checklist).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update")
    public ChecklistDTO updateChecklist(@RequestBody ChecklistDTO checklistDTO) {
        try {
            Checklist checklist = checklistDTO.toModel();
            return checklistService.update(checklist).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteChecklist(@PathVariable("id") Integer idChecklist) {
        return checklistService.deleteById(idChecklist);
    }

    @GetMapping("/getById/{id}")
    public ChecklistDTO getChecklistById(@PathVariable("id") Integer idChecklist) {
        return checklistService.findById(idChecklist).toDto();
    }
}