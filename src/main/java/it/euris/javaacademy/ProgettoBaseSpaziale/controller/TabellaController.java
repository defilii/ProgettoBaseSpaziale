package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TabellaDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
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
    public Boolean deleteTabella(@PathVariable("id") Integer idTabella) {
        return tabellaService.deleteById(idTabella);
    }

    @GetMapping("/v1/{id}")
    public TabellaDTO getTabellaById(@PathVariable("id") Integer idTabella) {
        return tabellaService.findById(idTabella).toDto();
    }
}