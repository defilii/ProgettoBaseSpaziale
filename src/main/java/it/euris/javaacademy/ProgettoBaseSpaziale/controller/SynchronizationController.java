package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.TrelloEntityListMaker;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@AllArgsConstructor
@RestController
@Data
@RequestMapping("/synchronize")
public class SynchronizationController {
    TabellaService tabellaService;
    @PutMapping("/synchronize")
    private List<Tabella> getInsert() {
        TrelloEntityListMaker client = new TrelloEntityListMaker();
        List<ListTrello> allList = client.allTrelloListFromJsonListWithReturn();
//
//        List<Card> allCards = client.allTrelloListFromJsonListWithReturn().stream()
//                .map(ListTrello::getId)
//                .map(stringId -> client.cardsFromJsonListIdWithReturn(stringId))
//                .flatMap(Collection::stream)
//                .toList();
//
        List<Tabella> allTables= allList.stream().map(ListTrello::toLocalEntity).toList();

        for (Tabella tabella : allTables) {
            tabellaService.insert(tabella);
        }

        return tabellaService.findAll();
    }
}
