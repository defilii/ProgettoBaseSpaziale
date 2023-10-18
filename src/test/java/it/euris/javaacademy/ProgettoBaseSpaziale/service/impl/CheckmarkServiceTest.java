package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CheckmarkRepository;
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
class CheckmarkServiceTest {
    @Mock
    CheckmarkRepository checkmarkRepository;

    @InjectMocks
    CheckmarkServiceImpl checkmarkService;

    @Test
    void shouldReturnACheckmark() {

        Checkmark checkmark = TestUtils.getCheckmark(1);

        List<Checkmark> checkmarks = List.of(checkmark);

        when(checkmarkRepository.findAll()).thenReturn(checkmarks);

        List<Checkmark> returnedCheckmark = checkmarkService.findAll();

        assertThat(returnedCheckmark)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withIntrospectionStrategy(new ComparingSnakeOrCamelCaseFields())
                .isEqualTo(checkmark);
    }

    @Test
    void shouldInsertACheckmark() {

        Checkmark checkmark = TestUtils.getCheckmark(null);


        when(checkmarkRepository.save(any())).thenReturn(checkmark);

        Checkmark returnedCheckmark = checkmarkService.insert(checkmark);
        assertThat(returnedCheckmark.getIdCheckmark())
                .isEqualTo(checkmark.getIdCheckmark());
    }

    @Test
    void shouldNotInsertAnyCheckmark() {

        Checkmark checkmark = TestUtils.getCheckmark(1);
        lenient().when(checkmarkRepository.save(any())).thenReturn(checkmark);

        assertThrows(IdMustBeNullException.class, () -> checkmarkService.insert(checkmark));

        assertThatThrownBy(() -> checkmarkService.insert(checkmark))
                .isInstanceOf(IdMustBeNullException.class);

    }

    @Test
    void shouldUpdateACheckmark() {

        Checkmark checkmark = TestUtils.getCheckmark(1);

        when(checkmarkRepository.save(any())).thenReturn(checkmark);

        Checkmark returnedCheckmark = checkmarkService.update(checkmark);
        assertThat(returnedCheckmark.getDescrizione())
                .isEqualTo(checkmark.getDescrizione());
    }

    @Test
    void shouldNotUpdateAnyCheckmark() {

        Checkmark checkmark = TestUtils.getCheckmark(null);
        lenient().when(checkmarkRepository.save(any())).thenReturn(checkmark);

        assertThatThrownBy(() -> checkmarkService.update(checkmark))
                .isInstanceOf(IdMustNotBeNullException.class);
    }

    @Test
    void shouldDeleteACheckmark() {
        //arrange
        Integer id = 3;

        doNothing().when(checkmarkRepository).deleteById(anyInt());
        when(checkmarkRepository.findById(id)).thenReturn(Optional.empty());
        assertTrue(checkmarkService.deleteById(id));
        Mockito.verify(checkmarkRepository, times(1)).deleteById(id);
    }
}