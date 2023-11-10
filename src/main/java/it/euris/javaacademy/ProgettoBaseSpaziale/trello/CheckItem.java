package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CheckItem implements TrelloEntity {

    private String id;

    private String state;
    private String name;

    private String idChecklist;
    @Override
    public Checkmark toLocalEntity() {
        return Checkmark.builder()
                .descrizione(name)
                .trelloId(id)
                .isItDone(state.equals("complete") ? true : false)
                .build();

    }
}
