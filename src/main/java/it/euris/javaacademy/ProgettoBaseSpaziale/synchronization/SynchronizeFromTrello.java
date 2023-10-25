package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checklist;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Checkmark;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ChecklistService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.CheckmarkService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SynchronizeFromTrello {

    TabellaService tabellaService;
    TaskService taskService;
    CheckmarkService checkmarkService;
    ChecklistService checklistService;
    private final TaskRepository taskRepository;
    private final TabellaRepository tabellaRepository;

    public SynchronizeFromTrello(TaskRepository taskRepository, TabellaRepository tabellaRepository, TaskService taskService, TabellaService tabellaService) {
        this.taskRepository = taskRepository;
        this.tabellaRepository = tabellaRepository;
        this.taskService = taskService;
        this.tabellaService = tabellaService;
    }

    public void updateAllTaskAndTabella() {

        TrelloCalls client = new TrelloCalls();
        List<ListTrello> allList = client.allTrelloListFromJsonListWithReturn();
        List<Card> allCard = allList.stream()
                .map(listTrello -> client.cardsFromJsonListId(listTrello.getId()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Tabella> allTabella = tabellaService.findAll();
        List<Task> allTasks = taskService.findAll();

        allList.stream()
                .forEach(trelloList ->
                {if (allTabella.stream()
                            .map(tabella -> tabella.getTrelloId())
                            .collect(Collectors.toList())
                            .contains(trelloList.getId())) {
                        Tabella tabellaToUpdate =
                                trelloList.toLocalEntity();
                        tabellaToUpdate.setId(tabellaRepository
                                .findByTrelloId(trelloList.getId()).getId());

                        tabellaService.update(tabellaToUpdate);
                    } else {
                        tabellaService.insert(trelloList.toLocalEntity());
                    }
                });

        allCard.stream().
                forEach(card ->
                {if (allTasks.stream()
                            .map(task -> task.getTrelloId())
                            .collect(Collectors.toList())
                            .contains(card.getId())) {
                        updateCard(card);
                    } else {
                        insertCard(card);
                    }
                });
    }

    private Task updateCard(Card card) {
        Task newTask = card.toLocalEntity();
        newTask.setIdTask(taskRepository
                .findByTrelloId(card.getId()).getIdTask());
        newTask.setTabella(taskRepository.findByTrelloId(card.getId()).getTabella());
        Task updatedTask = taskService.update(newTask);
        return updatedTask;
    }


    private Task insertCard(Card card) {
        Task newTask = card.toLocalEntity();
        newTask.setTabella(tabellaRepository.findByTrelloId(card.getIdList()));
        Task insertedTask = taskService.insert(newTask);
        insertChecklist(card, insertedTask);
        return insertedTask;
    }

    private void insertChecklist(Card card, Task insertedTask) {
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
