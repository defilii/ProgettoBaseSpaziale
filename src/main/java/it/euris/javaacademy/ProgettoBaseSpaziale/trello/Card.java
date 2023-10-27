package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import com.google.gson.Gson;
import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.UpdateTime;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.Exclude;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.localDateTimeToString;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;

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

    String lastUpdate;

    @Override
    public Task toLocalEntity() {
        return Task.builder()
                .taskName(name)
                .descrizione(desc)
                .dataScadenza(due == null ? null : ZonedDateTime.parse(due).toLocalDateTime())
                .lastUpdate(lastUpdate == null ? null
                        :ZonedDateTime.parse(String.valueOf(checkLastUpdate())).toLocalDateTime())
                .checklist(trelloChecklists == null ? null
                        : trelloChecklists.stream().map(TrelloChecklist::toLocalEntity).toList())
                .build();

    }

    @Override
    public LocalDateTime getLastUpdate() {
        return stringToLocalDateTime(lastUpdate);
    }

    @Override
    public LocalDateTime checkLastUpdate() {
        LocalDateTime cardLastUpdate = stringToLocalDateTime(lastUpdate);
        Task task = Task.builder().build();
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
