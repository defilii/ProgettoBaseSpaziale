package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.BoardIdMissingException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.ColorInputWrongOrNotSupportedException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyTokenOrUrl;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.RestCallUtils.*;

@AllArgsConstructor
@Service
public class LocalDBCalls {

    TaskRepository taskRepository;
    TabellaRepository tabellaRepository;
    TabellaService tabellaService;
    TaskService taskService;
    ApiKeyService apiKeyService;
    CommentoService commentoService;
    CommentoRepository commentoRepository;
    PriorityService priorityService;
    PriorityRepository priorityRepository;
    ChecklistRepository checklistRepository;
    ChecklistService checklistService;
    CheckmarkRepository checkmarkRepository;
    CheckmarkService checkmarkService;

    ConfigService configService;
    DeleteEntitiesOnTrello delete;

    private String getUrlFromConfig(String url) {
        return configService.findById(url).getUrl();
    }

    public void synchronize() {
        tabellaService.findAll().stream()
                .forEach(tabella -> {
                    try {
                        if (null == tabella.getTrelloId()) {
                            postNewTabella(tabella);
                        } else {
                            updateTabella(tabella);
                        }
                    } catch (InvalidKeyTokenOrUrl e) {
                        throw new RuntimeException(e);
                    }
                });

        taskService.findAll().stream()
                .forEach(task -> {
                    try {
                        if (null == task.getTrelloId()) {
                            postNewCard(task);
                        } else {
                            updateCard(task);
                        }
                    } catch (InvalidKeyTokenOrUrl e) {
                        throw new RuntimeException(e);
                    }
                });
        commentoService.findAll().stream()
                .forEach(commento -> {
                    try {
                        if (null == commento.getTrelloId()) {
                            postNewCommento(commento);
                        } else {
                            updateCommento(commento);
                        }
                    } catch (InvalidKeyTokenOrUrl e) {
                        throw new RuntimeException(e);
                    }
                });
        priorityService.findAll().stream()
                .forEach(priority -> {
                    try {
                        if (null == priority.getTrelloId()) {
                            postNewPriority(priority);
                        } else {
                            updatePriority(priority);
                        }
                    } catch (ColorInputWrongOrNotSupportedException | InvalidKeyTokenOrUrl e) {
                        throw new RuntimeException(e);
                    }
                });
        checklistRepository.findAll().stream()
                .forEach(checklist -> {
                    try {
                        if (null == checklist.getTrelloId()) {
                            postNewChecklist(checklist);
                        } else {
                            updateChecklist(checklist);
                        }
                    } catch (InvalidKeyTokenOrUrl e) {
                        throw new RuntimeException(e);
                    }
                });
        try {
            delete.deleteTrelloEntities();
        } catch (InvalidKeyTokenOrUrl e) {
            throw new RuntimeException(e);
        }
    }


    private void postNewCheckmark(Checkmark checkmark) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String checklistJson = gson.toJson(checkmark.toTrelloEntity());
        System.out.println(checklistJson);

        String urlAction = getUrlFromConfig("postNewCheckmark");
        String url = String.format(urlAction, checkmark.getChecklist().getTrelloId());
        String response = postJsonString(url, checklistJson, apiKeyService);
        System.out.println(response);
        CheckItem checkItem = gson.fromJson(response, CheckItem.class);
        checkmark.setTrelloId(checkItem.getId());
        checkmarkService.update(checkmark);
    }

    private void updateChecklist(Checklist checklist) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String checklistJson = gson.toJson(checklist.toTrelloEntity());
        String urlAction = getUrlFromConfig("updateChecklist");
        String url = String.format(urlAction, checklist.getTrelloId());

        putJsonString(url, checklistJson, apiKeyService);
        updateCheckItemsToTrello(checklist);

    }

    private void postNewChecklist(Checklist checklist) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String checklistJson = gson.toJson(checklist.toTrelloEntity());

        String urlAction = getUrlFromConfig("updateChecklist");
        String url = String.format(urlAction, checklist.getTask().getTrelloId());
        String response = postJsonString(url, checklistJson, apiKeyService);

        TrelloChecklist trelloChecklist = gson.fromJson(response, TrelloChecklist.class);
        checklist.setTrelloId(trelloChecklist.getId());
        checklistService.update(checklist);

        updateCheckItemsToTrello(checklist);
    }

    private void updateCheckItemsToTrello(Checklist checklist) throws InvalidKeyTokenOrUrl {
        String urlActionCheckmark = getUrlFromConfig("getCheckitemsFromChecklist");
        String urlCheckmark = String.format(urlActionCheckmark, checklist.getTrelloId());
        String arrayOfCheckitems = getJsonStringFromUrlGetCall(urlCheckmark, apiKeyService);
        System.out.println(arrayOfCheckitems);
        List<CheckItem> checkItemsToDelete = getList(arrayOfCheckitems, CheckItem.class);

        checkItemsToDelete.forEach(checkItem -> {
            String urlActionDeleteCheckmark = getUrlFromConfig("deleteCheckitemsFromChecklist");
            String urlDeleteCheckmark = String.format(urlActionDeleteCheckmark, checklist.getTrelloId(), checkItem.getId());
            try {
                deleteWithRestCall(urlDeleteCheckmark, apiKeyService);
            } catch (InvalidKeyTokenOrUrl e) {
                throw new RuntimeException(e);
            }
        });
        checklist.getChecklist()
                .forEach(checkItem -> {
                    try {
                        postNewCheckmark(checkItem);
                    } catch (InvalidKeyTokenOrUrl e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    public void updatePriority(Priority priority) throws ColorInputWrongOrNotSupportedException, InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String labelJson = gson.toJson(priority.toTrelloEntity());

        String urlAction = getUrlFromConfig("updatePriority");
        String url = String.format(urlAction, priority.getTrelloId());
        String response = putJsonString(url, labelJson, apiKeyService);

        if (response.contains("ERROR")) {
            throw new ColorInputWrongOrNotSupportedException();
        }
    }

    public void postNewPriority(Priority priority) throws ColorInputWrongOrNotSupportedException, InvalidKeyTokenOrUrl {
        Gson gson = new Gson();

        String boardId = getUrlFromConfig("boardId");
        String labelJson = gson.toJson(priority.toTrelloEntity());

        String urlAction = getUrlFromConfig("postNewPriority");
        String url = String.format(urlAction, boardId);
        String response = postJsonString(url, labelJson, apiKeyService);

        if (response.contains("ERROR")) {
            throw new ColorInputWrongOrNotSupportedException();
        }

        TrelloLabel label = gson.fromJson(response, TrelloLabel.class);
        priority.setTrelloId(label.getId());
        priorityService.update(priority);
    }


    private void updateCommento(Commento commento) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String urlAction = getUrlFromConfig("getCommento");
        String urlOfGet = String.format(urlAction,commento.getTrelloId());

        TrelloAction trelloAction = gson.fromJson(getJsonStringFromUrlGetCall(urlOfGet, apiKeyService), TrelloAction.class);
        if (commento.getLastUpdate().isAfter(stringToLocalDateTime(trelloAction.toLocalEntity().getLastUpdate().toString()))) {
            String urlActionUpdate = getUrlFromConfig("updateCommento");
            String url = String.format(urlActionUpdate, commento.getTrelloId(), commento.getCommento());
            putJsonString(url, null, apiKeyService);

        }
    }

    private void postNewCommento(Commento commento) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String idCard = commento.getTask().getTrelloId();

        String urlAction = getUrlFromConfig("postNewCommento");
        String url = String.format(urlAction, idCard, commento.getCommento());
        String response = postJsonString(url, null, apiKeyService);

        TrelloAction trelloAction = gson.fromJson(response, TrelloAction.class);
        commento.setTrelloId(trelloAction.getId());
        commentoService.update(commento);
    }


    private ListTrello postNewTabella(Tabella tabella) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();

        String idBoard = tabellaService.findAll().stream()
                .map(Tabella::getTrelloBoardId)
                .filter(Objects::nonNull)
                .findFirst().orElse(getUrlFromConfig("boardId"));

        String urlAction = getUrlFromConfig("postNewTabella");
        String url = String.format(urlAction, tabella.getNome(), idBoard);
        String response = postJsonString(url, null, apiKeyService);

        ListTrello listTrello = gson.fromJson(response, ListTrello.class);
        tabella.setTrelloId(listTrello.getId());
        tabella.setTrelloBoardId(listTrello.getIdBoard());
        tabellaService.update(tabella);
        return listTrello;
    }

    private ListTrello updateTabella(Tabella tabella) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();


        String urlAction = getUrlFromConfig("updateNewTabella");
        String url = String.format(urlAction, tabella.getTrelloId());
        String listJson = gson.toJson(tabella.toTrelloEntity());
        String response = putJsonString(url, listJson, apiKeyService);

        ListTrello listTrello = gson.fromJson(response, ListTrello.class);
        return listTrello;
    }

    public Card postNewCard(Task task) throws InvalidKeyTokenOrUrl {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String cardJson = gson.toJson(task.toTrelloEntity());

        String listId = task.getTabella().getTrelloId();

        String urlAction = getUrlFromConfig("postNewCard");
        String url = String.format(urlAction, listId);
        String response = postJsonString(url, cardJson, apiKeyService);

        Card card = gson.fromJson(response, Card.class);
        task.setTrelloId(card.getId());
        task.setTrelloListId(card.getIdList());
        taskService.update(task);
        return card;

    }

    private Card updateCard(Task task) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String idCard = task.getTrelloId();

        String urlOfActionGet = getUrlFromConfig("getCard");
        String urlOfGet = String.format(urlOfActionGet, task.getTrelloId());
        Card trelloCard = gson.fromJson(getJsonStringFromUrlGetCall(urlOfGet, apiKeyService), Card.class);
        if (task.getLastUpdate().isAfter(stringToLocalDateTime(trelloCard.toLocalEntity().getLastUpdate().toString()))) {
            Card cardToUpdate = task.toTrelloEntity();
            cardToUpdate.setIdList(task.getTabella().getTrelloId());

            String cardJson = gson.toJson(cardToUpdate);
            String urlAction = getUrlFromConfig("updateCard");
            String url = String.format(urlAction, idCard);
            String response = putJsonString(url, cardJson, apiKeyService);

            return gson.fromJson(response, Card.class);

        }
        return task.toTrelloEntity();
    }

}
