package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.key.TaskHasUserKey;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskHasUserRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskHasUserService;
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
public class TaskHasUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskHasUserService taskHasUserService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskHasUserRepository taskHasUserRepository;


    @Test
    void shouldGetOneTaskHasUser() throws Exception {

        Integer id = 1;
        TaskHasUser taskHasUser = TestUtils.getTaskHasUserSingleId(id);
        List<TaskHasUser> taskHasUsers = List.of(taskHasUser);

        when(taskHasUserService.findAll()).thenReturn(taskHasUsers);


        mockMvc.perform(MockMvcRequestBuilders.get("/task-has-users/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldInsertATaskHasUser() throws Exception {
        Integer id = 1;
        TaskHasUser taskHasUser = TestUtils.getTaskHasUserSingleId(id);
        when(taskHasUserService.insert(any())).thenReturn(taskHasUser);

        mockMvc.perform(post("/task-has-users/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskHasUser.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void shouldUpdateATaskHasUser() throws Exception {
        Integer id = 1;
        TaskHasUser taskHasUser = TestUtils.getTaskHasUserSingleId(id);
        when(taskHasUserService.update(any())).thenReturn(taskHasUser);

        mockMvc.perform(put("/task-has-users/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskHasUser.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }


    @Test
    void shouldDelete() throws Exception {
        Integer id = 1;
        TaskHasUser taskHasUser = TestUtils.getTaskHasUserSingleId(id);
        taskHasUser.setTaskHasUserKey(TaskHasUserKey.builder()
                        .taskId(id)
                        .userId(id)
                .build());

        when(taskHasUserService.deleteById(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/task-has-users/v1/{id}", taskHasUser.getTaskHasUserKey())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
