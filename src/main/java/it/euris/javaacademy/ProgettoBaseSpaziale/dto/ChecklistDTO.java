package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDTO implements Dto {

    private Integer idChecklist;

    private String nome;

    private Task task;
    @Override
    public Checklist toModel() {
        return Checklist.builder()
                .idChecklist(idChecklist)
                .task(task)
                .nome(nome)
                .build();
    }
}
