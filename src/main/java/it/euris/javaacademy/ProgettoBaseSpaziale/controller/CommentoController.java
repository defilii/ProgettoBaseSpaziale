package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CommentoDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TabellaDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CommentoRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CommentoService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/commenti")
public class CommentoController {
    CommentoService commentoService;
    private final CommentoRepository commentoRepository;
    TaskService taskService;
    UserService userService;

    @GetMapping("/getAll")
    @Operation(description = """
            This method is used to retrieve all the comments from the database<br>
            """)
    public List<CommentoDTO> getAllComments() {
        return commentoService.findAll().stream().map(Commento::toDto).toList();
    }

    @PostMapping("/insert")
    public CommentoDTO saveCommento(@RequestBody CommentoDTO commentoDTO) {
        try {
            Commento commento = commentoDTO.toModel();
            return commentoService.insert(commento).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update")
    public CommentoDTO updateCommento(@RequestBody CommentoDTO commentoDTO) {
        try {
            Commento commento = commentoDTO.toModel();
            return commentoService.update(commento).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteCommento(@PathVariable("id") Integer idCommento) {
        return commentoService.deleteById(idCommento);
    }

    @GetMapping("/getById/{id}")
    public CommentoDTO getCommentoById(@PathVariable("id") Integer idCommento) {
        return commentoService.findById(idCommento).toDto();
    }

    @GetMapping("/v1/all-comments-by-idTabella/{id}")
    public List<CommentoDTO> getAllCommentiByTabellaId(@PathVariable("id") Integer idTabella) {
        return commentoService.findAll().stream()
                .filter(commento -> commento.getTask().getTabella().getId().equals(idTabella))
                .map(Commento::toDto)
                .toList();
    }

    @GetMapping("/v1/last-comment-by-idTabella/{id}")
    public List<CommentoDTO> getLastCommentoByTabellaId(@PathVariable("id") Integer idTabella) {
        return commentoService.findAll().stream()
                .filter(commento -> commento.getTask().getTabella().getId().equals(idTabella))
                .max(Comparator.comparing(commento -> commento.getDataCommento().toEpochSecond(ZoneOffset.ofHours(0))))
                .stream()
                .map(Commento::toDto)
                .toList();
    }

    @PostMapping("v1/add-new-comment/{id-task}-{id-user}")
    @Operation(description = """
            This method is used to add a new comment to the database<br>
            """)
    public CommentoDTO addNewCommenti(@PathVariable("id-task") Integer idTask, @PathVariable("id-user") Integer idUser, @RequestBody CommentoDTO commentoDTO) {

        Commento commento = commentoDTO.toModel();
        commento.setTask(taskService.findById(idTask));
        commento.setUser(userService.findById(idUser));
        try {
            return commentoService.insert(commento).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}