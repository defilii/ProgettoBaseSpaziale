package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyTokenOrUrl;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Getter
public class AllListFromRestAndDB {

    List<ListTrello> allList;
    List<Card> allCard;
    List<TrelloChecklist> allTrelloChecklist;
    List<CheckItem> allCheckitems;
    List<Tabella> allTabella;
    List<Task> allTasks;
    List<Checklist> allChecklist;
    List<Checkmark> allCheckmark;
    List<Priority> allPriority;
    List<TrelloLabel> allLabel;
    List<Members> allMembers;
    List<User> allUser;
    List<Commento> allComments;
    List<TrelloAction> allTrelloActions;

    ApiKeyService apiKeyService;

    TaskRepository taskRepository;
    TabellaRepository tabellaRepository;
    TabellaService tabellaService;
    TaskService taskService;
    CommentoService commentoService;
    CommentoRepository commentoRepository;
    PriorityService priorityService;
    PriorityRepository priorityRepository;
    ChecklistService checklistService;
    CheckmarkService checkmarkService;
    UserRepository userRepository;
    ConfigService configService;

    public void updateList() throws InvalidKeyTokenOrUrl {
        TrelloCalls client = new TrelloCalls(apiKeyService, configService);
        allList = client.allTrelloListFromJsonListWithReturn();
        allCard = allList.stream()
                .map(listTrello -> {
                    try {
                        return client.cardsFromJsonListId(listTrello.getId());
                    } catch (InvalidKeyTokenOrUrl e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allLabel = client.getAllTrelloLabels();
        allMembers = client.getAllMembers();
        allTrelloChecklist = allCard.stream()
                .map(Card::getTrelloChecklists)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allCheckitems = allTrelloChecklist.stream()
                .map(TrelloChecklist::getCheckItems)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allTrelloActions = allCard.stream()
                .map(Card::getTrelloActions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        allTabella = tabellaService.findAll();
        allTasks = taskService.findAll();
        allComments = commentoService.findAll();
        allChecklist = checklistService.findAll();
        allCheckmark = checkmarkService.findAll();
        allPriority = priorityService.findAll();
        allUser = userRepository.findAll();
    }
}
