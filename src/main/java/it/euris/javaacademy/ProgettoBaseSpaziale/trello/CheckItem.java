package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CheckItem {


    private String idCheckItem;

    private String state;

}
