package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
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
class TabellaServiceTest {

    @Mock
    TabellaRepository tabellaRepository;

    @InjectMocks
    TabellaServiceImpl tabellaService;

    @Test
    void shouldReturnATabella(){

        Tabella tabella = TestUtils.getTabella(1);

        List<Tabella> tabelle = List.of(tabella);

        when(tabellaRepository.findAll()).thenReturn(tabelle);

        List<Tabella> returnedTabelle = tabellaService.findAll();

        assertThat(returnedTabelle)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withIntrospectionStrategy(new ComparingSnakeOrCamelCaseFields())
                .isEqualTo(tabella);
    }

    @Test
    void shouldInsertATabella(){

        Tabella tabella =TestUtils.getTabella(null);

        when(tabellaRepository.save(any())).thenReturn(tabella);

        Tabella returnedTabella = tabellaService.insert(tabella);
        assertThat(returnedTabella.getId())
                .isEqualTo(tabella.getId());
    }

    @Test
    void shouldNotInsertAnyTabella(){

        Tabella tabella = TestUtils.getTabella(1);
        lenient().when(tabellaRepository.save(any())).thenReturn(tabella);

        assertThrows(IdMustBeNullException.class, () -> tabellaService.insert(tabella));

    }

    @Test
    void shouldUpdateATabella(){

        Tabella tabella = TestUtils.getTabella(1);

        when(tabellaRepository.save(any())).thenReturn(tabella);

        Tabella returnedTabella = tabellaService.update(tabella);
        assertThat(returnedTabella.getNome())
                .isEqualTo(tabella.getNome());
    }

    @Test
    void shouldNotUpdateAnyTabella(){

        Tabella tabella = TestUtils.getTabella(null);
        lenient().when(tabellaRepository.save(any())).thenReturn(tabella);

        assertThatThrownBy(() -> tabellaService.update(tabella))
                .isInstanceOf(IdMustNotBeNullException.class);
    }

    @Test
    void shouldDeleteATabella() {
        //arrange
        Integer id = 3;

        doNothing().when(tabellaRepository).deleteById(anyInt());
        when(tabellaRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(tabellaService.deleteById(id));
        Mockito.verify(tabellaRepository, times(1)).deleteById(id);
    }
}