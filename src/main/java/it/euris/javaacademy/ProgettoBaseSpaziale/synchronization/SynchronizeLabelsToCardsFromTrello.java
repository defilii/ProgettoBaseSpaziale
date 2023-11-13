package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyTokenOrUrl;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.PriorityRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.PriorityService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Service
public class SynchronizeLabelsToCardsFromTrello {
    List<Card> allCard;
    PriorityService priorityService;
    PriorityRepository priorityRepository;
    TaskRepository taskRepository;
    TaskService taskService;

    AllListFromRestAndDB allListFromRestAndDB;

    public void updateLabelsToCard() {
        try {
            allListFromRestAndDB.updateList();
        } catch (InvalidKeyTokenOrUrl e) {
            throw new RuntimeException(e);
        }
        allCard = allListFromRestAndDB.getAllCard();
        allCard.stream().forEach(card -> {
                    List<String> idLabels = card.getIdLabels();
                    for (String idLabel : idLabels) {
                        System.out.println(card.getId());
                        Task task = taskRepository.findByTrelloId(card.getId()) == null ? Task.builder().build() : taskRepository.findByTrelloId(card.getId());
                        Priority priority = priorityRepository.findByTrelloId(idLabel);
                        if (!task.getPriorities().contains(priority)) {
                            List<Priority> priorityList = new LinkedList<>(task.getPriorities());
                            priorityList.add(priority);
                            task.setPriorities(priorityList);

                            taskRepository.save(task);
                        }
                    }
                }
        );
    }
}
