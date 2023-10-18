package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.UserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @GetMapping("/v1")
    @Operation(description = """
            This method is used to retrieve all the users from the database<br>
            """)
    public List<UserDTO> getAllUser() {
        return userService.findAll().stream().map(User::toDto).toList();
    }

    @GetMapping("/v1/{id}")
    @Operation(description = """
            This method is used to retrieve one user from the database<br>
            """)
    public UserDTO getUserById(@PathVariable("id") Integer idUser) {
        return userService.findById(idUser).toDto();
    }

    @PostMapping("/v1")
    @Operation(description = """
            This method is used to save one user in the database<br>
            """)
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userDTO.toModel();
            return userService.insert(user).toDto();
        } catch (IdMustBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/v1")
    @Operation(description = """
            This method is used to update one user in the database<br>
            """)
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userDTO.toModel();
            return userService.update(user).toDto();
        } catch (IdMustNotBeNullException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/v1/{id}")
    @Operation(description = """
            This method is used to delete one user from the database<br>
            """)
    public Boolean deleteUser(@PathVariable("id") Integer idUser) {
        return userService.deleteById(idUser);
    }
}
