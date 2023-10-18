package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
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
public class ChecklistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChecklistService checklistService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ChecklistRepository checklistRepository;


    @Test
    void shouldGetOneChecklist() throws Exception {

        Integer id = 1;
        Checklist checklist = TestUtils.getCheckList(id);
        List<Checklist> checklists = List.of(checklist);

        when(checklistService.findAll()).thenReturn(checklists);


        mockMvc.perform(MockMvcRequestBuilders.get("/checklists/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idChecklist").value(checklist.getIdChecklist()));
    }

    @Test
    void shouldInsertAChecklist() throws Exception {
        Integer id = 1;
        Checklist checklist = TestUtils.getCheckList(id);
        when(checklistService.insert(any())).thenReturn(checklist);

        mockMvc.perform(post("/checklists/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checklist.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idChecklist").value(checklist.getIdChecklist()));
    }

    @Test
    void shouldUpdateAChecklist() throws Exception {
        Integer id = 1;
        Checklist checklists = TestUtils.getCheckList(id);
        when(checklistService.update(any())).thenReturn(checklists);

        mockMvc.perform(put("/checklists/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checklists.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idChecklist").value(checklists.getIdChecklist()));
    }


    @Test
    void shouldDelete() throws Exception {
        Integer id = 1;
        Checklist checklist = TestUtils.getCheckList(id);

        when(checklistService.deleteById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/checklists/v1/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}