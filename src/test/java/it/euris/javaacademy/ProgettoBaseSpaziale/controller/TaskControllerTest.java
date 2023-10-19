package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.TaskHasUser;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
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
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;


    @Test
    void shouldGetOneTask() throws Exception {

        Integer id = 1;
        Task task = TestUtils.getTask(id);
        List<Task> tasks = List.of(task);

        when(taskService.findAll()).thenReturn(tasks);


        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idTask").value(task.getIdTask()));
    }

    @Test
    void shouldInsertATask() throws Exception {
        Integer id = 1;
        Task Task = TestUtils.getTask(id);
        when(taskService.insert(any())).thenReturn(Task);

        mockMvc.perform(post("/tasks/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Task.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idTask").value(Task.getIdTask()));
    }

    @Test
    void shouldUpdateATask() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        when(taskService.update(any())).thenReturn(task);

        mockMvc.perform(put("/tasks/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idTask").value(task.getIdTask()));
    }


    @Test
    void shouldDelete() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);

        when(taskService.deleteById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/v1/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("GIVEN a task WHEN using the get priority url THEN I should get the priority")
    void shouldGetPriority() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        when(taskService.findById(id)).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/v1/priorita/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value(task.getPriorita().toString()));

    }

    @Test
    @DisplayName("GIVEN a task with no priority WHEN using the get priority url THEN I should get a set message")
    void shouldntGetPriority() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        task.setPriorita(null);
        when(taskService.findById(id)).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/v1/priorita/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value("no priority set"));

    }

    @Test
    @DisplayName("GIVEN a task WHEN using the get priority url THEN I should get the expire date")
    void shouldGetExpireDate() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        when(taskService.findById(id)).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/v1/expiredate/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value(task.getDataScadenza().toString()));

    }

    @Test
    @DisplayName("GIVEN a task with no expire date WHEN using the get priority url THEN I should get a set message")
    void shouldntGetExpiredate() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        task.setDataScadenza(null);
        when(taskService.findById(id)).thenReturn(task);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/v1/expiredate/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value("no expire date set"));

    }


    @Test
    @DisplayName("GIVEN a task with members WHEN using the get members url THEN I should get the list of members")
    void shouldGetMembers() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);

        User user = TestUtils.getUser(1);
        TaskHasUser taskHasUser = TestUtils.getTaskHasUserSingleId(user);
        List<TaskHasUser> taskHasUsers = List.of(taskHasUser);
        task.setUsersTask(taskHasUsers);

        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/v1/members/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].idUser").value(user.getIdUser()));

        ;

    }

    @Test
    @DisplayName("GIVEN a task with no members WHEN using the get members url THEN I should get nothing")
    void shouldntGetMembers() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);

        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/v1/members/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isEmpty());

    }
}
