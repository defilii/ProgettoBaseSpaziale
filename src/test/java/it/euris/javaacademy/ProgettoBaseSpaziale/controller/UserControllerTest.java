package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.UserRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.UserService;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldGetOneUser() throws Exception {

        Integer id = 1;
        User User = TestUtils.getUser(id);
        List<User> Users = List.of(User);

        when(userService.findAll()).thenReturn(Users);


        mockMvc.perform(MockMvcRequestBuilders.get("/users/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idUser").value(User.getIdUser()));
    }

    @Test
    void shouldInsertAUser() throws Exception {
        Integer id = 1;
        User User = TestUtils.getUser(id);
        when(userService.insert(any())).thenReturn(User);

        mockMvc.perform(post("/users/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idUser").value(User.getIdUser()));
    }

    @Test
    void shouldUpdateAUser() throws Exception {
        Integer id = 1;
        User User = TestUtils.getUser(id);
        when(userService.update(any())).thenReturn(User);

        mockMvc.perform(put("/users/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idUser").value(User.getIdUser()));
    }


    @Test
    void shouldDelete() throws Exception {
        Integer id = 1;
        User User = TestUtils.getUser(id);

        when(userService.deleteById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/v1/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
