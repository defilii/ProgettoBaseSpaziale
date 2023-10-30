package it.euris.javaacademy.ProgettoBaseSpaziale.entity.pre_insert;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.TaskDTO;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Model;
import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.PreInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskInsert implements PreInsert {

private Integer id;
private String taskName;
private String descrizione;
private TabellaInsert tabella;



    @Override
    public Task toModel() {
        return Task.builder()
                .idTask(id)
                .tabella(tabella.toModel())
                .taskName(taskName)
                .descrizione(descrizione)
                .build();
    }
}
