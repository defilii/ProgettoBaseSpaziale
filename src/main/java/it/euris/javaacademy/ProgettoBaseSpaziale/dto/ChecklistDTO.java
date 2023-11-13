package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import lombok.*;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToInteger;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistDTO implements Dto {

    private Integer idChecklist;

    private String nome;

    private String taskId;
    private String taskName;

    private String lastUpdate;
    private String trelloId;

    @Override
    public Checklist toModel() {
        return Checklist.builder()
                .idChecklist(idChecklist)
                .task(Task.builder().idTask(stringToInteger(taskId)).build())
                .nome(nome)
                .trelloId(trelloId)
                .build();
    }
}
