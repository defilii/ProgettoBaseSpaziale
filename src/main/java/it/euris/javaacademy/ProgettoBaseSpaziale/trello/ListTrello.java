package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.Exclude;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListTrello implements TrelloEntity {


    private String id;

    private String name;

    private String idBoard;
    @Exclude
    private String localId;

    private Boolean closed;

    @Override
    public Tabella toLocalEntity() {
        return Tabella
                .builder()
                .trelloId(id)
                .nome(name)
                .build();
    }
}
