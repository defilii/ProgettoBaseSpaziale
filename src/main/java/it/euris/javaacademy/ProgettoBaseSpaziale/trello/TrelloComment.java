package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrelloComment {
    private String text;
    private String dateLastEdited;


}
