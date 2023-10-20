package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.CheckmarkDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/checkmarks")
public class CheckmarkController {
    CheckmarkService checkmarkService;

    @GetMapping("/getAll")
    @Operation(description = """
            This method is used to retrieve all the checkmarks from the database<br>
            """)
    public List<CheckmarkDTO> getAllCheckmarks() {
        return checkmarkService.findAll().stream().map(Checkmark::toDto).toList();
    }

    @PostMapping("/insert")
    public CheckmarkDTO saveCheckmark(@RequestBody CheckmarkDTO checkmarkDTO) {
        try {
            Checkmark checkmark = checkmarkDTO.toModel();
            return checkmarkService.insert(checkmark).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update")
    public CheckmarkDTO updateCheckMark(@RequestBody CheckmarkDTO checkmarkDTO) {
        try {
            Checkmark checkmark = checkmarkDTO.toModel();
            return checkmarkService.update(checkmark).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Boolean deleteCheckmark(@PathVariable("id") Integer idCheckmark) {
        return checkmarkService.deleteById(idCheckmark);
    }

    @GetMapping("/getById/{id}")
    public CheckmarkDTO getCheckmarkById(@PathVariable("id") Integer idCheckmark) {
        return checkmarkService.findById(idCheckmark).toDto();
    }
}