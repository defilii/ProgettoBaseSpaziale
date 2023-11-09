package it.euris.javaacademy.ProgettoBaseSpaziale.entity.pre_insert;

import it.euris.javaacademy.ProgettoBaseSpaziale.dto.archetype.PreInsert;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import lombok.*;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.numberToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskInsert implements PreInsert {

    private Integer id;//
    private String taskName;
    private String descrizione;
    private TabellaInsert tabella;
//    private String trelloId;

    @Override
    public Task toModel() {
        return Task.builder()
                .idTask(id)//
                .tabella(tabella.toModel())
                .taskName(getTaskName())
                .descrizione(getDescrizione())
//                .trelloId(getTrelloId())
                .build();
    }
}
