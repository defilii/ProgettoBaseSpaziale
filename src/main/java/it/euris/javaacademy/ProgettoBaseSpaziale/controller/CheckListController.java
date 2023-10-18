package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.ChecklistDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CheckmarkDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}