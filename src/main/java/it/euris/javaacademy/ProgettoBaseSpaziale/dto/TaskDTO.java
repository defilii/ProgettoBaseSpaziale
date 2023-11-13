package it.euris.javaacademy.ProgettoBaseSpaziale.dto;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.Dto;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import lombok.*;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDTO implements Dto {

    private Integer idTask;

    private String taskName;

    private String priorita;

    private String descrizione;

    private String dataScadenza;
    private String trelloId;
    private String trelloListId;
    private Tabella tabella;
    private String lastUpdate;

    @Override
    public Task toModel() {
        return Task.builder()
                .idTask(idTask)
                .tabella(tabella)
                .taskName(taskName)
                .descrizione(descrizione)
                .dataScadenza(stringToLocalDateTime(dataScadenza))
                .trelloId(trelloId)
                .trelloListId(trelloListId)
                .build();
    }
}
