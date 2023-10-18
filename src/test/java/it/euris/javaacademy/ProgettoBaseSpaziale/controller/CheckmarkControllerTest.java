package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CheckmarkRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
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
public class CheckmarkControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CheckmarkService checkmarkService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CheckmarkRepository checkmarkRepository;


    @Test
    void shouldGetOneCheckmark() throws Exception {

        Integer id = 1;
        Checkmark checkmark = TestUtils.getCheckmark(id);
        List<Checkmark> checkmarks = List.of(checkmark);

        when(checkmarkService.findAll()).thenReturn(checkmarks);


        mockMvc.perform(MockMvcRequestBuilders.get("/checkmarks/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idCheckmark").value(checkmark.getIdCheckmark()));
    }

    @Test
    void shouldInsertACheckmark() throws Exception {
        Integer id = 1;
        Checkmark checkmark = TestUtils.getCheckmark(id);
        when(checkmarkService.insert(any())).thenReturn(checkmark);

        mockMvc.perform(post("/checkmarks/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkmark.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idCheckmark").value(checkmark.getIdCheckmark()));
    }

    @Test
    void shouldUpdateACheckmark() throws Exception {
        Integer id = 1;
        Checkmark checkmark = TestUtils.getCheckmark(id);
        when(checkmarkService.update(any())).thenReturn(checkmark);

        mockMvc.perform(put("/checkmarks/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkmark.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idCheckmark").value(checkmark.getIdCheckmark()));
    }


    @Test
    void shouldDelete() throws Exception {
        Integer id = 1;
        Checkmark checkmark = TestUtils.getCheckmark(id);

        when(checkmarkService.deleteById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/checkmarks/v1/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}