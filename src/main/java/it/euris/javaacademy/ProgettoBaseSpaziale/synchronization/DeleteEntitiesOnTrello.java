package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyTokenOrUrl;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.RestCallUtils.deleteWithRestCall;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.RestCallUtils.putJsonString;

@Service
@AllArgsConstructor
public class DeleteEntitiesOnTrello {

    TaskRepository taskRepository;
    TabellaRepository tabellaRepository;
    TabellaService tabellaService;
    TaskService taskService;
    ApiKeyService apiKeyService;
    CommentoService commentoService;
    CommentoRepository commentoRepository;
    PriorityService priorityService;
    PriorityRepository priorityRepository;
    ChecklistService checklistService;
    CheckmarkService checkmarkService;
    UserRepository userRepository;

    AllListFromRestAndDB allListFromRestAndDB;

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

    public void deleteTrelloEntities() throws InvalidKeyTokenOrUrl {
        updateList();
        allTrelloActions.stream()
                .forEach(trelloAction ->
                {
                    if (commentoRepository.findByTrelloId(trelloAction.getId()) == null) {
                        try { deleteCommento(trelloAction);
                        } catch (InvalidKeyTokenOrUrl e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        allCard.stream()
                .forEach(card ->
                {
                    if (taskRepository.findByTrelloId(card.getId()) == null) {
                        try { deleteCard(card.toLocalEntity());
                        } catch (InvalidKeyTokenOrUrl e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        allList.stream()
                .forEach(list ->
                {
                    if (tabellaRepository.findByTrelloId(list.getId()) == null) {
                        try { deleteTabella(list.toLocalEntity());
                        } catch (InvalidKeyTokenOrUrl e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        allLabel.stream()
                .forEach(priority ->
                {
                    if (priorityRepository.findByTrelloId(priority.getId()) == null) {
                        try { deletePriority(priority.toLocalEntity());
                        } catch (InvalidKeyTokenOrUrl e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    public void deletePriority(Priority priority) throws InvalidKeyTokenOrUrl {
        String url = "https://api.trello.com/1/labels/" + priority.getTrelloId();
        deleteWithRestCall(url, apiKeyService);
    }

    private void deleteCommento(TrelloAction trelloAction) throws InvalidKeyTokenOrUrl {
        String idCard = trelloAction.getData().getCard().getId();

        String url = "https://api.trello.com/1/cards/" + idCard + "/actions/" + trelloAction.getId() + "/comments";
        System.out.println(url);
        deleteWithRestCall(url, apiKeyService);
    }

    private void deleteCard(Task task) throws InvalidKeyTokenOrUrl {
        String idCard = task.getTrelloId();

        String url = "https://api.trello.com/1/cards/" + idCard;
        deleteWithRestCall(url, apiKeyService);
    }

    private ListTrello deleteTabella(Tabella tabella) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();

        String url = "https://api.trello.com/1/lists/" + tabella.getTrelloId();

        ListTrello trelloEntity = tabella.toTrelloEntity();
        trelloEntity.setClosed(true);
        String listJson = gson.toJson(trelloEntity);
        String response = putJsonString(url, listJson, apiKeyService);

        ListTrello listTrello = gson.fromJson(response, ListTrello.class);
        return listTrello;
    }

    private void updateList() throws InvalidKeyTokenOrUrl {
        allListFromRestAndDB.updateList();
        allList = allListFromRestAndDB.getAllList();
        allCard = allListFromRestAndDB.allCard;
        allLabel = allListFromRestAndDB.allLabel;
        allMembers = allListFromRestAndDB.allMembers;
        allTrelloChecklist = allListFromRestAndDB.allTrelloChecklist;
        allCheckitems = allListFromRestAndDB.allCheckitems;
        allTrelloActions = allListFromRestAndDB.allTrelloActions;
        allTabella = allListFromRestAndDB.allTabella;
        allTasks = allListFromRestAndDB.allTasks;
        allComments = allListFromRestAndDB.allComments;
        allChecklist = allListFromRestAndDB.allChecklist;
        allCheckmark = allListFromRestAndDB.allCheckmark;
        allPriority = allListFromRestAndDB.allPriority;
        allUser = allListFromRestAndDB.allUser;
    }
}
