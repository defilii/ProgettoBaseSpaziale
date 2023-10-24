package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CommentoRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CommentoService;
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
public class CommentoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentoService commentoService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentoRepository CommentoRepository;


    @Test
    void shouldGetOneCommento() throws Exception {

        Integer id = 1;
        Commento commento = TestUtils.getCommento(id);
        List<Commento> commenti = List.of(commento);

        when(commentoService.findAll()).thenReturn(commenti);


        mockMvc.perform(MockMvcRequestBuilders.get("/commenti/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idCommento").value(commento.getIdCommento()));
    }

    @Test
    void shouldInsertACommento() throws Exception {
        Integer id = 1;
        Commento commento = TestUtils.getCommento(id);
        when(commentoService.insert(any())).thenReturn(commento);

        mockMvc.perform(post("/commenti/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commento.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idCommento").value(commento.getIdCommento()));
    }

    @Test
    void shouldUpdateACommento() throws Exception {
        Integer id = 1;
        Commento commento = TestUtils.getCommento(id);
        when(commentoService.update(any())).thenReturn(commento);

        mockMvc.perform(put("/commenti/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commento.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idCommento").value(commento.getIdCommento()));
    }


    @Test
    void shouldDelete() throws Exception {
        Integer id = 1;
        Commento commento = TestUtils.getCommento(id);

        when(commentoService.deleteById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/commenti/v1/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void shouldGetAllCommentiByIdTabella() throws Exception {
        Integer id = 1;
        Commento commento = TestUtils.getCommento(1);
        Tabella tabella = TestUtils.getTabella(id);

        List<Commento> commenti = List.of(commento);
        when(commentoService.findAll()).thenReturn(commenti);

        mockMvc.perform(MockMvcRequestBuilders.get("/commenti/v1/all-comments-by-idTabella/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].dataCommento").value(commento.getDataCommento().toString()));
    }

    @Test
    void shouldGetLastCommentByIdTabella() throws Exception {
        Integer id = 1;
        Commento commento = TestUtils.getCommento(1);
        Tabella tabella = TestUtils.getTabella(id);

        List<Commento> commenti = List.of(commento);
        when(commentoService.findAll()).thenReturn(commenti);

        mockMvc.perform(MockMvcRequestBuilders.get("/commenti/v1/last-comment-by-idTabella/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].dataCommento").value(commento.getDataCommento().toString()));
    }

    @Test
    @DisplayName("GIVEN a commento, with id user and id task  WHEN insert commento THEN should add a new commento")
    void shouldAddANewCommento() throws Exception {
        Integer id = 1;
        Integer idTask= 2;
        Integer idUser=2;
        Commento commento = TestUtils.getCommentoUserTask(id, idTask,idUser);
        when(commentoService.insert(any())).thenReturn(commento);

        mockMvc.perform(post("/commenti/v1/add-new-comment/{id-task}-{id-user}", idTask, idUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commento.toDto())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.idCommento").value(commento.getIdCommento()));
    }
}
