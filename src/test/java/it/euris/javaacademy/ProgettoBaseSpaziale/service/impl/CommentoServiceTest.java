package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CommentoRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.TestUtils;
import org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentoServiceTest {
    @Mock
    CommentoRepository commentoRepository;
    @InjectMocks
    CommentoServiceImpl commentoService;

    @Test
    void shouldReturnACommento() {

        Commento commento = TestUtils.getCommento(1);

        List<Commento> commenti = List.of(commento);

        when(commentoRepository.findAll()).thenReturn(commenti);

        List<Commento> returnedCommetni = commentoService.findAll();

        assertThat(returnedCommetni)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withIntrospectionStrategy(new ComparingSnakeOrCamelCaseFields())
                .isEqualTo(commento);
    }

    @Test
    void shouldInsertACommento() {

        Commento commento = TestUtils.getCommento(null);

        when(commentoRepository.save(any())).thenReturn(commento);

        Commento returnedCommetni = commentoService.insert(commento);
        assertThat(returnedCommetni.getIdCommento())
                .isEqualTo(commento.getIdCommento());
    }

    @Test
    void shouldNotInsertAnyCommento() {

        Commento commento = TestUtils.getCommento(1);
        lenient().when(commentoRepository.save(any())).thenReturn(commento);

        assertThrows(IdMustBeNullException.class, () -> commentoService.insert(commento));

    }

    @Test
    void shouldUpdateACommento() {

        Commento commento = TestUtils.getCommento(1);

        when(commentoRepository.save(any())).thenReturn(commento);

        Commento returnedCommetno = commentoService.update(commento);
        assertThat(returnedCommetno.getCommento())
                .isEqualTo(commento.getCommento());
    }

    @Test
    void shouldNotUpdateAnyCommento() {

        Commento commento = TestUtils.getCommento(null);
        lenient().when(commentoRepository.save(any())).thenReturn(commento);

        assertThatThrownBy(() -> commentoService.update(commento))
                .isInstanceOf(IdMustNotBeNullException.class);
    }

    @Test
    void shouldDeleteACommento() {
        //arrange
        Integer id = 3;

        doNothing().when(commentoRepository).deleteById(anyInt());
        when(commentoRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(commentoService.deleteById(id));
        Mockito.verify(commentoRepository, times(1)).deleteById(id);
    }
}