package it.euris.javaacademy.ProgettoBaseSpaziale.controller;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CheckmarkRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.SynchronizeFromTrello;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.TrelloCalls;
import it.euris.javaacademy.ProgettoBaseSpaziale.synchronization.TrelloEntityListMaker;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
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
    TaskService taskService;
    CheckmarkService checkmarkService;
    ChecklistService checklistService;
    CheckmarkRepository checkmarkRepository;
    ChecklistRepository checklistRepository;
    TaskRepository taskRepository;
    TabellaRepository tabellaRepository;

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

    @PutMapping("/synchronize")
    private void inserts() {
        TrelloCalls client = new TrelloCalls();
        List<ListTrello> allList = client.allTrelloListFromJsonListWithReturn();


        for (ListTrello listTrello : allList) {

            tabellaService.findAll().stream()
                    .map(Tabella::getTrelloId)
                    .filter(trelloIdSavedLocally ->
                            listTrello.getId().equals(trelloIdSavedLocally));

            Tabella tabellaSalvata = tabellaService.insert(listTrello.toLocalEntity());
            listTrello.setLocalId(String.valueOf(tabellaSalvata.getId()));
            List<Card> allCard = client.cardsFromJsonListId(listTrello.getId());
            for (Card card : allCard) {
                Task newTask = card.toLocalEntity();
                newTask.setTabella(tabellaSalvata);
                Task insertedTask = taskService.insert(newTask);

                List<Checklist> newChecklists = card.toLocalEntity().getChecklist();
                for (Checklist checklist :
                        newChecklists) {
                    checklist.setTask(insertedTask);
                    Checklist insertedChecklist = checklistService.insert(checklist);
                    List<Checkmark> newCheckmarks = checklist.getChecklist();
                    for (Checkmark checkmark :
                            newCheckmarks) {
                        checkmark.setChecklist(insertedChecklist);
                        checkmarkService.insert(checkmark);
                    }
                }
            }
        }

    }

    @PutMapping("/v2/synchronize")
    private void insertsSmooth() {
        SynchronizeFromTrello synchronizeFromTrello = new SynchronizeFromTrello(
                taskRepository, tabellaRepository, taskService, tabellaService,checkmarkService, checkmarkRepository, checklistService, checklistRepository
        );
        synchronizeFromTrello.updateAllTaskAndTabella();
    }
}