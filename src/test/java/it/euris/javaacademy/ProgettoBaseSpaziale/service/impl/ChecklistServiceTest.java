package it.euris.javaacademy.ProgettoBaseSpaziale.service.impl;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.IdMustNotBeNullException;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChecklistServiceTest {
    @Mock
    ChecklistRepository checklistRepository;

    @InjectMocks
    ChecklistServiceImpl checklistService;

    @Test
    void shouldReturnAChecklist() {
        Checklist checklist = TestUtils.getCheckList(1);
        List<Checklist> checklists = List.of(checklist);

        when(checklistRepository.findAll()).thenReturn(checklists);
        List<Checklist> returnChecklists = checklistService.findAll();

        assertThat(returnChecklists)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .withIntrospectionStrategy(new ComparingSnakeOrCamelCaseFields())
                .isEqualTo(checklist);
    }

    @Test
    void shoulInsertAChecklist(){
        Checklist checklist = TestUtils.getCheckList(null);
        when(checklistRepository.save(any())).thenReturn(checklist);

        Checklist returnChecklist= checklistService.insert(checklist);

        assertThat(returnChecklist.getIdChecklist())
                .isEqualTo(checklist.getIdChecklist());
    }

    @Test
    void shouldNotInsertAnyCChecklist() {

        Checklist checklist = TestUtils.getCheckList(1);
        lenient().when(checklistRepository.save(any())).thenReturn(checklist);

        assertThrows(IdMustBeNullException.class, () -> checklistService.insert(checklist));

        assertThatThrownBy(() -> checklistService.insert(checklist))
                .isInstanceOf(IdMustBeNullException.class);

    }

    @Test
    void shouldUpdateChecklist(){
        Checklist checklist = TestUtils.getCheckList(1);
        when(checklistRepository.save(any())).thenReturn(checklist);

        Checklist returnedChecklist=checklistService.update(checklist);
        assertThat(returnedChecklist.getNome())
                .isEqualTo(checklist.getNome());
    }

    @Test
    void shouldNotUpdateAnyCheckmark() {

        Checklist checklist =TestUtils.getCheckList(null);
        lenient().when(checklistRepository.save(any())).thenReturn(checklist);

        assertThatThrownBy(() -> checklistService.update(checklist))
                .isInstanceOf(IdMustNotBeNullException.class);
    }


    @Test
    void shouldDeleteAChecklist(){
        Integer id = 3;

        doNothing().when(checklistRepository).deleteById(anyInt());
        when(checklistRepository.findById(id)).thenReturn(Optional.empty());

        assertTrue(checklistService.deleteById(id));
        Mockito.verify(checklistRepository, times(1)).deleteById(id);
    }
}