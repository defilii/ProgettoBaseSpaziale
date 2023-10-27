package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.ChecklistRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CheckmarkRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.CheckItem;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.TrelloChecklist;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public class SynchronizeFromTrello {

    TabellaService tabellaService;
    TaskService taskService;

    CheckmarkService checkmarkService;
    ChecklistService checklistService;
    ChecklistRepository checklistrepository;
    CheckmarkRepository checkmarkRepository;

    TaskRepository taskRepository;
    TabellaRepository tabellaRepository;

    List<ListTrello> allList;
    List<Card> allCard;
    List<TrelloChecklist> allTrelloChecklist;
    List<CheckItem> allCheckitems;
    List<Tabella> allTabella;
    List<Task> allTasks;
    List<Checklist> allChecklist;
    List<Checkmark> allCheckmark;

    public SynchronizeFromTrello(TaskRepository taskRepository, TabellaRepository tabellaRepository, TaskService taskService, TabellaService tabellaService, CheckmarkService checkmarkService, CheckmarkRepository checkmarkRepository, ChecklistService checklistService, ChecklistRepository checklistRepository) {
        this.taskRepository = taskRepository;
        this.tabellaRepository = tabellaRepository;
        this.taskService = taskService;
        this.tabellaService = tabellaService;
        this.checkmarkService = checkmarkService;
        this.checklistrepository = checklistRepository;
        this.checkmarkRepository = checkmarkRepository;
        this.checklistService = checklistService;
    }

    public void updateAllTaskAndTabella() {
        updateList();
        String idBoardToSet = allList.stream().map(ListTrello::getIdBoard).findAny().orElse(null);

        allList.stream()
                .forEach(trelloList ->
                {
                    if (allTabella.stream()
                            .map(tabella -> tabella.getTrelloId())
                            .collect(Collectors.toList())
                            .contains(trelloList.getId())) {
                        Tabella tabellaToUpdate =
                                trelloList.toLocalEntity();
                        tabellaToUpdate.setId(tabellaRepository
                                .findByTrelloId(trelloList.getId()).getId());
                        tabellaToUpdate.setTrelloBoardId(idBoardToSet);
                        tabellaService.update(tabellaToUpdate);
                    } else {
                        Tabella tabellaToInsert = trelloList.toLocalEntity();
                        tabellaToInsert.setTrelloBoardId(idBoardToSet);
                        tabellaService.insert(tabellaToInsert);
                    }
                });

        allCard.stream().
                forEach(card ->
                {
                    if (allTasks.stream()
                            .map(task -> task.getTrelloId())
                            .collect(Collectors.toList())
                            .contains(card.getId())) {
                        updateCard(card);
                    } else {
                        insertCard(card);
                    }
                });

        deleteFromDatabase();
    }

    private void updateList() {
        TrelloCalls client = new TrelloCalls();
        allList = client.allTrelloListFromJsonListWithReturn();
        allCard = allList.stream()
                .map(listTrello -> client.cardsFromJsonListId(listTrello.getId()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allTrelloChecklist = allCard.stream()
                .map(card -> card.getTrelloChecklists())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allCheckitems = allTrelloChecklist.stream()
                .map(card -> card.getCheckItems())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allTabella = tabellaService.findAll();
        allTasks = taskService.findAll();
        allChecklist = checklistService.findAll();
        allCheckmark = checkmarkService.findAll();
    }

    private void deleteFromDatabase() {
        allCheckmark.stream()
                .forEach(checkmark ->
                {
                    if (!allCheckitems.stream()
                            .map(checkitem -> checkitem.getIdCheckItem())
                            .collect(Collectors.toList()).contains(checkmark.getTrelloId())) {
                        checkmarkService.deleteById(checkmark.getIdCheckmark());
                    }
                });
        allChecklist.stream()
                .forEach(checklist ->
                {
                    if (!allTrelloChecklist.stream()
                            .map(trelloChecklist -> trelloChecklist.getId())
                            .collect(Collectors.toList()).contains(checklist.getTrelloId())) {
                        checklistService.deleteById(checklist.getIdChecklist());
                    }
                });
        allTasks.stream()
                .forEach(task ->
                {
                    if (!allCard.stream()
                            .map(card -> card.getId())
                            .collect(Collectors.toList()).contains(task.getTrelloId())) {
                        taskService.deleteById(task.getIdTask());
                    }
                });

        allTabella.stream()
                .forEach(tabella ->
                {
                    if (!allList.stream()
                            .map(listTrello -> listTrello.getId())
                            .collect(Collectors.toList()).contains(tabella.getTrelloId())) {
                        tabellaService.deleteById(tabella.getId());
                    }
                });


    }

    private Task updateCard(Card card) {
        Task newTask = card.toLocalEntity();
        newTask.setIdTask(taskRepository
                .findByTrelloId(card.getId()).getIdTask());
        newTask.setTabella(tabellaRepository.findByTrelloId(card.getIdList()));
        Task updatedTask = taskService.update(newTask);
        insertChecklist(card, updatedTask);
        return updatedTask;
    }


    private Task insertCard(Card card) {
        Task newTask = card.toLocalEntity();
        newTask.setTabella(tabellaRepository.findByTrelloId(card.getIdList()));
        Task insertedTask = taskService.insert(newTask);
        insertChecklist(card, insertedTask);
        return insertedTask;
    }


    private void insertChecklist(Card card, Task updatedTask) {
        List<TrelloChecklist> newChecklists = card.getTrelloChecklists();
        for (TrelloChecklist trelloChecklist :
                newChecklists) {
            Checklist checklistToInsert = trelloChecklist.toLocalEntity();
            checklistToInsert.setTask(updatedTask);
            if (allChecklist.stream()
                    .map(checklist -> checklist.getTrelloId())
                    .collect(Collectors.toList())
                    .contains(trelloChecklist.getId())) {

                checklistToInsert.setIdChecklist(checklistrepository
                        .findByTrelloId(trelloChecklist.getId()).getIdChecklist());
                Checklist updatedChecklist = checklistService.update(checklistToInsert);

//                insertCheckmark(trelloChecklist,updatedChecklist);
            } else {
                Checklist insertedChecklist = checklistService.insert(checklistToInsert);

//                insertCheckmark(trelloChecklist,insertedChecklist);
            }
        }
    }

    private void insertCheckmark(TrelloChecklist trelloChecklist, Checklist insertedChecklist) {
        List<CheckItem> checkitems = trelloChecklist.getCheckItems();
        for (CheckItem checkitem :
                checkitems) {
            Checkmark checkmarkToInsert = checkitem.toLocalEntity();
            checkmarkToInsert.setChecklist(insertedChecklist);
            if (allCheckmark.stream()
                    .map(checkmark -> checkmark.getTrelloId())
                    .collect(Collectors.toList())
                    .contains(checkitem.getIdCheckItem())) {

                checkmarkToInsert.setIdCheckmark(checkmarkRepository
                        .findByTrelloId(checkitem.getIdCheckItem()).getIdCheckmark());
                Checkmark updatedCheckmark = checkmarkService.update(checkmarkToInsert);

            } else {
                Checkmark insertedCheckmark = checkmarkService.insert(checkmarkToInsert);
            }
        }

    }

}
