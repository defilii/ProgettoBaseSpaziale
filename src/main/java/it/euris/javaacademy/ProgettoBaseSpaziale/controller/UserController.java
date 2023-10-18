package it.euris.javaacademy.ProgettoBaseSpaziale.controller;
import io.swagger.v3.oas.annotations.Operation;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.UserDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
