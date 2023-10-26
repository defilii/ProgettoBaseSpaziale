package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.Exclude;
import lombok.*;

import java.awt.*;
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
    private String dateLastActivity;
    private List<TrelloLabel> labels;

    @Exclude
    private String localId;

    private List<String> idMembers = new ArrayList<>();

    private List<TrelloChecklist> trelloChecklists = new ArrayList<>();


    @Override
    public Task toLocalEntity() {
        return Task.builder()
                .taskName(name)
                .descrizione(desc)
                .dataScadenza(due== null ? null : ZonedDateTime.parse(due).toLocalDateTime())
                .lastUpdate(ZonedDateTime.parse(dateLastActivity).toLocalDateTime())
                .checklist(trelloChecklists.stream().map(TrelloChecklist::toLocalEntity).toList())
                .trelloId(id)
                .priorita(labels.stream().map(labels -> labels.toPriority()).findAny().orElse(null))
                .build();

    }
}
