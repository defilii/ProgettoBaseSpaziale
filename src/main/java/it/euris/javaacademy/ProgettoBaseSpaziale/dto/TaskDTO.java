package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToPriorita;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO implements Dto {

    private Integer idTask;

    private String taskName;

    private String priorita;

    private String descrizione;

    private String dataScadenza;

    private Tabella tabella;

    @Override
    public Task toModel() {
        return Task.builder()
                .idTask(idTask)
                .tabella(tabella)
                .taskName(taskName)
                .priorita(stringToPriorita(priorita))
                .descrizione(descrizione)
                .dataScadenza(stringToLocalDateTime(dataScadenza))
                .build();
    }
}
