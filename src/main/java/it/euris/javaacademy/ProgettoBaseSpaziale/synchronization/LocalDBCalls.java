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

import java.util.Objects;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.Converter.stringToLocalDateTime;
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

    DeleteEntitiesOnTrello delete;


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
//        checkmarkRepository.findAll().stream()
//                .forEach(priority -> {
//                    try {
//                        if (null == priority.getTrelloId()) {
//                            postNewPriority(priority);
//                        } else {
//                            updatePriority(priority);
//                        }
//                    } catch (ColorInputWrongOrNotSupportedException | InvalidKeyTokenOrUrl e) {
//                        throw new RuntimeException(e);
//                    }
//                });
        try {
            delete.deleteTrelloEntities();
        } catch (InvalidKeyTokenOrUrl e) {
            throw new RuntimeException(e);
        }
    }

    private void updateChecklist(Checklist checklist) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String checklistJson = gson.toJson(checklist.toTrelloEntity());

        String url = "https://api.trello.com/1/checklists/" + checklist.getTrelloId();
        putJsonString(url, checklistJson, apiKeyService);

    }

    private void postNewChecklist(Checklist checklist) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String checklistJson = gson.toJson(checklist.toTrelloEntity());

        String url = "https://api.trello.com/1/checklists?idCard=" + checklist.getTask().getTrelloId();
        String response = postJsonString(url, checklistJson, apiKeyService);

        TrelloChecklist trelloChecklist = gson.fromJson(response, TrelloChecklist.class);
        checklist.setTrelloId(trelloChecklist.getId());
        checklistService.update(checklist);
    }


    public void updatePriority(Priority priority) throws ColorInputWrongOrNotSupportedException, InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String labelJson = gson.toJson(priority.toTrelloEntity());

        String url = "https://api.trello.com/1/labels/" + priority.getTrelloId();
        String response = putJsonString(url, labelJson, apiKeyService);

        if (response.contains("ERROR")) {
            throw new ColorInputWrongOrNotSupportedException();
        }
    }

    public void postNewPriority(Priority priority) throws ColorInputWrongOrNotSupportedException, InvalidKeyTokenOrUrl {
        Gson gson = new Gson();

        String boardId = "652d5727a3301d21fa288a27";
        String labelJson = gson.toJson(priority.toTrelloEntity());

        String url = "https://api.trello.com/1/labels?idBoard=" + boardId;
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
        String urlOfGet = "https://api.trello.com/1/actions/" + commento.getTrelloId();
        TrelloAction trelloAction = gson.fromJson(getJsonStringFromUrlGetCall(urlOfGet, apiKeyService), TrelloAction.class);
        if (commento.getLastUpdate().isAfter(stringToLocalDateTime(trelloAction.toLocalEntity().getLastUpdate().toString()))) {
            String url = "https://api.trello.com/1/actions/" + commento.getTrelloId() + "?text=" + commento.getCommento();
            putJsonString(url, null, apiKeyService);

        }
    }

    private void postNewCommento(Commento commento) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String idCard = commento.getTask().getTrelloId();

        String url = "https://api.trello.com/1/cards/" + idCard + "/actions/comments?text=" + commento.getCommento();
        String response = postJsonString(url, null, apiKeyService);

        TrelloAction trelloAction = gson.fromJson(response, TrelloAction.class);
        commento.setTrelloId(trelloAction.getId());
        commentoService.update(commento);
    }


    private ListTrello postNewTabella(Tabella tabella) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();
        String check = "missing board id";
        String idBoard = tabellaService.findAll().stream()
                .map(Tabella::getTrelloBoardId)
                .filter(Objects::nonNull)
                .findFirst().orElse(check);
        if (idBoard.equals(check)) {
            throw new BoardIdMissingException();
        }
        String url = "https://api.trello.com/1/lists?name=" + tabella.getNome() + "&idBoard=" + idBoard;
        String response = postJsonString(url, null, apiKeyService);

        ListTrello listTrello = gson.fromJson(response, ListTrello.class);
        tabella.setTrelloId(listTrello.getId());
        tabella.setTrelloBoardId(listTrello.getIdBoard());
        tabellaService.update(tabella);
        return listTrello;
    }

    private ListTrello updateTabella(Tabella tabella) throws InvalidKeyTokenOrUrl {
        Gson gson = new Gson();

        String url = "https://api.trello.com/1/lists/" + tabella.getTrelloId();
        String listJson = gson.toJson(tabella.toTrelloEntity());
        String response = putJsonString(url, listJson, apiKeyService);

        ListTrello listTrello = gson.fromJson(response, ListTrello.class);
        return listTrello;
    }

    public Card postNewCard(Task task) throws InvalidKeyTokenOrUrl {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String cardJson = gson.toJson(task.toTrelloEntity());

        String listId = task.getTabella().getTrelloId();

        String url = "https://api.trello.com/1/cards?idList=" + listId;
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

        String urlOfGet = "https://api.trello.com/1/cards/" + task.getTrelloId();
        Card trelloCard = gson.fromJson(getJsonStringFromUrlGetCall(urlOfGet, apiKeyService), Card.class);
        if (task.getLastUpdate().isAfter(stringToLocalDateTime(trelloCard.toLocalEntity().getLastUpdate().toString()))) {
            Card cardToUpdate = task.toTrelloEntity();
            cardToUpdate.setIdList(task.getTabella().getTrelloId());

            String cardJson = gson.toJson(cardToUpdate);
            String url = "https://api.trello.com/1/cards/" + idCard;
            String response = putJsonString(url, cardJson, apiKeyService);

            return gson.fromJson(response, Card.class);

        }
        return task.toTrelloEntity();
    }


    //TODO add method to update and post checkmark and checklist, with delete too

}
