package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Card {

    private String id;

    private String name;

    private String desc;

    private String idList;

    private String due;

    private List<String> idMembers = new ArrayList<>();

    private List<CheckItem> checkItems = new ArrayList<>();
}
