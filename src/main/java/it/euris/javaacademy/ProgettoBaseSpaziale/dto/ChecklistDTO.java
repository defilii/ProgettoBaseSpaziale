package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import lombok.*;

import java.time.LocalDateTime;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDTO implements Dto {

    private Integer idChecklist;

    private String nome;

    private Task task;

    private String lastUpdate;
    private String trelloId;

    @Override
    public Checklist toModel() {
        return Checklist.builder()
                .idChecklist(idChecklist)
                .task(task)
                .nome(nome)
                .lastUpdate(stringToLocalDateTime(lastUpdate))
                .trelloId(trelloId)
                .build();
    }
}
