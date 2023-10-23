package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListTrello {


    private String id;

    private String name;

    private String idBoard;

    private Boolean closed;
}
