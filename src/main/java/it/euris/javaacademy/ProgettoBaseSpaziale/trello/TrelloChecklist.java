package it.euris.javaacademy.ProgettoBaseSpaziale.trello;

import it.euris.javaacademy.ProgettoBaseSpaziale.converter.TrelloEntity;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrelloChecklist implements TrelloEntity {
    String id;
    String name;
    String idCard;
    String lastUpdate;
    List<CheckItem> checkItems = new ArrayList();

    @Override
    public Checklist toLocalEntity() {
        return Checklist.builder()
                .nome(name)
                .trelloId(id)
                .checklist(checkItems.stream().map(CheckItem::toLocalEntity).toList())
                .build();
    }
}
