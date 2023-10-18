package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
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
public class TabellaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TabellaService tabellaService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TabellaRepository tabellaRepository;


    @Test
    void shouldGetOneTabella() throws Exception {

        Integer id = 1;
        Tabella tabella = TestUtils.getTabella(id);
        List<Tabella> tabelle = List.of(tabella);

        when(tabellaService.findAll()).thenReturn(tabelle);


        mockMvc.perform(MockMvcRequestBuilders.get("/tabelle/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(tabella.getId()));
    }

    @Test
    void shouldInsertATabella() throws Exception {
        Integer id = 1;
        Tabella tabella = TestUtils.getTabella(id);
        when(tabellaService.insert(any())).thenReturn(tabella);

        mockMvc.perform(post("/tabelle/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tabella.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(tabella.getId()));
    }

    @Test
    void shouldUpdateATabella() throws Exception {
        Integer id = 1;
        Tabella tabella = TestUtils.getTabella(id);
        when(tabellaService.update(any())).thenReturn(tabella);

        mockMvc.perform(put("/tabelle/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tabella.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(tabella.getId()));
    }


    @Test
    void shouldDelete() throws Exception {
        Integer id = 1;
        Tabella tabella = TestUtils.getTabella(id);

        when(tabellaService.deleteById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tabelle/v1/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
