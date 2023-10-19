package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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


        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/getAll"))
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

        mockMvc.perform(post("/tasks/insert")
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

        mockMvc.perform(put("/tasks/update")
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
                        .delete("/tasks/delete/{id}", id)
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

    @Test
    @DisplayName("GIVEN a task with a checklist, with 3 true checkmarks and a false one WHEN using the get percentage url THEN I should get 75")
    void shouldGetPercentage() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        Checklist checklist = TestUtils.getCheckList(1);

        List<Checkmark> fakeCheckMarkList = new ArrayList<>();
        fakeCheckMarkList.add(Checkmark.builder().idCheckmark(1).isItDone(true).build());
        fakeCheckMarkList.add(Checkmark.builder().idCheckmark(2).isItDone(true).build());
        fakeCheckMarkList.add(Checkmark.builder().idCheckmark(3).isItDone(true).build());
        fakeCheckMarkList.add(Checkmark.builder().idCheckmark(4).isItDone(false).build());
        checklist.setChecklist(fakeCheckMarkList);
        task.setChecklist(List.of(checklist));


        when(taskService.findById(id)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/v1/percentage/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value("75%"));

    }

    @Test
    @DisplayName("GIVEN a task WHEN using the post id description rest url THEN I should update the task's description")
    void shouldUpdateATaskDescription() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        when(taskService.update(any())).thenReturn(task);
        when(taskService.findById(id)).thenReturn(task);

        String newDescription = "nuova descrizione";
        mockMvc.perform(put("/tasks/v1/update-description/{id}-{description}", id, newDescription)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idTask").value(task.getIdTask()))
                .andExpect(jsonPath("$.descrizione").value(newDescription))

        ;
    }

    @Test
    @DisplayName("GIVEN a task WHEN using the put id expiredate rest url THEN I should update the task's expire date")
    void shouldUpdateATaskExpireDate() throws Exception {
        Integer id = 1;
        Task task = TestUtils.getTask(id);
        when(taskService.update(any())).thenReturn(task);
        when(taskService.findById(id)).thenReturn(task);

    Integer day = 1;
    Integer month = 1;
    Integer year = 1;
    LocalDateTime date = LocalDateTime.of(year, month, day, 0 ,0);
        mockMvc.perform(put("/tasks/v1/update-date/{id}-{dayOfMonth}-{month}-{year}", id, day, month, year)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idTask").value(task.getIdTask()))
                .andExpect(jsonPath("$.dataScadenza").value(date.toString()))

        ;
    }

}
