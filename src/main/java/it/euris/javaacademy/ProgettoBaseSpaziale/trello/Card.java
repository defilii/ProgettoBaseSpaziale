package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.Exclude;
import lombok.*;

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
                .trelloListId(idList)
                .priorita(labels.stream().map(labels -> labels.toPriority()).findAny().orElse(null))
                .build();

    }
}
