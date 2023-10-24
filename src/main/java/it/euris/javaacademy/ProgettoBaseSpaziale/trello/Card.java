package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.LocalEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter;
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
public class Card implements TrelloEntity {

    TabellaService tabellaService;

    private String id;

    private String name;

    private String desc;

    private String idList;

    private String due;

    private Integer localId;

    private List<String> idMembers = new ArrayList<>();

    private List<CheckItem> checkItems = new ArrayList<>();

    @Override
    public Task toLocalEntity() {
        return Task.builder()
                .taskName(name)
                .descrizione(desc)
                .dataScadenza(Converter.stringToLocalDateTime(due))
                .lastUpdate(LocalDateTime.now())
                .build();

    }
}
