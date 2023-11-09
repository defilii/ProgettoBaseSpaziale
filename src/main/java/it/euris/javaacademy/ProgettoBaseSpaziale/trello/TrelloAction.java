package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrelloAction implements TrelloEntity {

    private String id;
    private String idMemberCreator;
    private TrelloComment data;
    private String date;

    @Override
    public Commento toLocalEntity() {
        return Commento.builder()
                .trelloId(id)
                .dataCommento(ZonedDateTime.parse(date).toLocalDateTime())
                .commento(data.getText())
                .lastUpdate(null == data.getDateLastEdited() ? OffsetDateTime.parse(date).toLocalDateTime() : OffsetDateTime.parse(data.getDateLastEdited()).toLocalDateTime())
                .build();
    }
}
