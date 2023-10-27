package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrelloLabel {
    private String id;
    private String name;
    public Priorita toPriority() {
        switch (name) {
            case "Bassa Priorità":
                return Priorita.BASSA;

            case "Alta Priorità":
                return Priorita.ALTA;

            case "Media Priorità":
                return Priorita.MEDIA;

            case "Desiderata":
                return Priorita.DESIDERATA;

            default:
                return null;
        }
    }
}
