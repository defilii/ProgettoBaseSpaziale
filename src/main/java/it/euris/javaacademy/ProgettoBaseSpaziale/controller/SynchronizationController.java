package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.User;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.UserService;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.TrelloCalls;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.TrelloEntityListMaker;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Members;
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
    UserService userService;

    @PutMapping("/v1/synchronize")
    private List<Tabella> getInsertOld() {
        TrelloEntityListMaker client = new TrelloEntityListMaker();
        List<ListTrello> allList = client.allTrelloListFromJsonListWithReturn();
//
//        List<Card> allCards = client.allTrelloListFromJsonListWithReturn().stream()
//                .map(ListTrello::getId)
//                .map(stringId -> client.cardsFromJsonListIdWithReturn(stringId))
//                .flatMap(Collection::stream)
//                .toList();
//
        List<Tabella> allTables = allList.stream().map(ListTrello::toLocalEntity).toList();

        for (Tabella tabella : allTables) {
            Tabella tabellaSalvata = tabellaService.insert(tabella);

        }

        return tabellaService.findAll();
    }

    @PutMapping("/synchronizetabella")
    private List<Tabella> getInsert() {
        TrelloEntityListMaker client = new TrelloEntityListMaker();
        List<ListTrello> allList = client.allTrelloListFromJsonListWithReturn();

        for (ListTrello listTrello : allList) {
            Tabella tabellaSalvata = tabellaService.insert(listTrello.toLocalEntity());
            listTrello.setLocalId(String.valueOf(tabellaSalvata.getId()));
        }

        return tabellaService.findAll();
    }

    @PutMapping("/synchronizemembers")
    private List<User> getInserts() {
        TrelloCalls client = new TrelloCalls();
        List<Members> allMembers = client.membersFromJsonListWithReturn();

        for (Members listMember : allMembers) {
            User userSalvato = userService.insert(listMember.toLocalEntity());
            listMember.setLocalId(String.valueOf(userSalvato.getIdUser()));
        }

        return userService.findAll();
    }
}
