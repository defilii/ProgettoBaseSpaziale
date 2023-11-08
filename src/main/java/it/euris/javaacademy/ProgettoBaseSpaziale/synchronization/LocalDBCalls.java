package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.BoardIdMissingException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.ColorInputWrongOrNotSupportedException;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyOrToken;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.CommentoRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.PriorityRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.TrelloAction;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.TrelloLabel;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.RestCallUtils.postJsonString;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.RestCallUtils.putJsonString;

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

    public List<Card> trelloGetCardsOnABoard() {
        String boardId = "652d5727a3301d21fa288a27";
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" +
                        boardId +
                        "/cards")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();


        System.out.println(response.getBody().toPrettyString());

        List<Card> cards = getList(response.getBody().toString(), Card.class);
        return cards;
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
                    } catch (InvalidKeyOrToken e) {
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
                    } catch (InvalidKeyOrToken e) {
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
                    } catch (InvalidKeyOrToken e) {
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
                    } catch (ColorInputWrongOrNotSupportedException | InvalidKeyOrToken e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void updatePriority(Priority priority) throws ColorInputWrongOrNotSupportedException, InvalidKeyOrToken {
        Gson gson = new Gson();
        String labelJson = gson.toJson(priority.toTrelloEntity());

        String url = "https://api.trello.com/1/labels/" + priority.getTrelloId();
        String response = putJsonString(url, labelJson, apiKeyService);

        if (response.contains("ERROR")) {
            throw new ColorInputWrongOrNotSupportedException();
        }
    }

    public void postNewPriority(Priority priority) throws ColorInputWrongOrNotSupportedException, InvalidKeyOrToken {
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

    private void updateCommento(Commento commento) throws InvalidKeyOrToken {
        Gson gson = new Gson();

        String url = "https://api.trello.com/1/actions/" + commento.getTrelloId() + "?text=" + commento.getCommento();
        putJsonString(url, null, apiKeyService);
    }

    private void postNewCommento(Commento commento) throws InvalidKeyOrToken {
        Gson gson = new Gson();
        String idCard = commento.getTask().getTrelloId();

        String url = "https://api.trello.com/1/cards/" + idCard + "/actions/comments?text=" +  commento.getCommento();
        String response = postJsonString(url, null, apiKeyService);

        TrelloAction trelloAction = gson.fromJson(response, TrelloAction.class);
        commento.setTrelloId(trelloAction.getId());
        commentoService.update(commento);
    }

    private ListTrello postNewTabella(Tabella tabella) throws InvalidKeyOrToken {
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
        String response = postJsonString(url,null, apiKeyService);

        ListTrello listTrello = gson.fromJson(response, ListTrello.class);
        tabella.setTrelloId(listTrello.getId());
        tabella.setTrelloBoardId(listTrello.getIdBoard());
        tabellaService.update(tabella);
        return listTrello;
    }

    private ListTrello updateTabella(Tabella tabella) throws InvalidKeyOrToken {
        Gson gson = new Gson();

        String url = "https://api.trello.com/1/lists/" + tabella.getTrelloId();
        String listJson = gson.toJson(tabella.toTrelloEntity());
        String response = putJsonString(url, listJson, apiKeyService);

        ListTrello listTrello = gson.fromJson(response, ListTrello.class);
        return listTrello;
    }

    public Card postNewCard(Task task) throws InvalidKeyOrToken {
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

    private Card updateCard(Task task) throws InvalidKeyOrToken {
        Gson gson = new Gson();
        String idCard = task.getTrelloId();

        Card cardToUpdate = task.toTrelloEntity();
        cardToUpdate.setIdList(task.getTabella().getTrelloId());

        String cardJson = gson.toJson(cardToUpdate);
        String url = "https://api.trello.com/1/cards/" + idCard;
        String response = putJsonString(url, cardJson, apiKeyService);

        return gson.fromJson(response, Card.class);
    }

    //TODO tidy this mess up and expose only the method to synchronize in a separate controller after you finish implementing the synchronization of checklist and checkmark

}
