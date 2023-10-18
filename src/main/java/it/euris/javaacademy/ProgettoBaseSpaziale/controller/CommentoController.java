package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CommentoDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CommentoDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TabellaDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CommentoService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/commenti")
public class CommentoController {
    CommentoService commentoService;

    @GetMapping("/v1")
    @Operation(description = """
            This method is used to retrieve all the comments from the database<br>
            """)
    public List<CommentoDTO> getAllComments() {
        return commentoService.findAll().stream().map(Commento::toDto).toList();
    }

    @PostMapping("/v1")
    public CommentoDTO saveCommento(@RequestBody CommentoDTO commentoDTO) {
        try {
            Commento commento = commentoDTO.toModel();
            return commentoService.insert(commento).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/v1")
    public CommentoDTO updateCommento(@RequestBody CommentoDTO commentoDTO) {
        try {
            Commento commento = commentoDTO.toModel();
            return commentoService.update(commento).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/v1/{id}")
    public Boolean deleteCommento(@PathVariable("id") Integer idCommento) {
        return commentoService.deleteById(idCommento);
    }

    @GetMapping("/v1/{id}")
    public CommentoDTO getCommentoById(@PathVariable("id") Integer idCommento) {
        return commentoService.findById(idCommento).toDto();
    }
}