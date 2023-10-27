package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.UpdateTime;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.Exclude;
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
public class Card implements TrelloEntity, UpdateTime {

    TabellaService tabellaService;

    private String id;

    private String name;

    private String desc;

    private String idList;

    private String due;

    @Exclude
    private String localId;

    private List<String> idMembers = new ArrayList<>();

    private List<TrelloChecklist> trelloChecklists = new ArrayList<>();

    LocalDateTime lastUpdate;

    @Override
    public Task toLocalEntity() {
        return Task.builder()
                .taskName(name)
                .descrizione(desc)
                .dataScadenza(due == null ? null : ZonedDateTime.parse(due).toLocalDateTime())
                .lastUpdate(getLastUpdate())
                .checklist(trelloChecklists == null? null
                        :trelloChecklists.stream().map(TrelloChecklist::toLocalEntity).toList())
                .build();

    }

    @Override
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public LocalDateTime checkLastUpdate() {
        LocalDateTime cardLastUpdate = lastUpdate;
        Task task = toLocalEntity();
        LocalDateTime taskLastUpdate = (task == null) ? null : task.getLastUpdate();
        if (taskLastUpdate == null) {
            return cardLastUpdate;
        } else if (taskLastUpdate.isAfter(cardLastUpdate)) {
            return taskLastUpdate;
        } else {
            return cardLastUpdate;
        }
    }
}
