package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrelloLabel implements TrelloEntity {
    private String id;
    private String name;
    private String color;

    @Override
    public Priority toLocalEntity() {
        return Priority.builder()
                .trelloId(id)
                .name(name)
                .color(color)
                .build()
                ;
    }
}
