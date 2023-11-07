package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Commento;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Priority;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.BoardIdMissingException;
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

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;

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
                    if (null == tabella.getTrelloId()) {
                        postNewTabella(tabella);
                    } else {
                        updateTabella(tabella);
                    }
                });

        taskService.findAll().stream()
                .forEach(task -> {
                    if (null == task.getTrelloId()) {
                        postNewCard(task);
                    } else {
                        updateCard(task);
                    }
                });
        commentoService.findAll().stream()
                .forEach(commento -> {
                    if (null == commento.getTrelloId()) {
                        postNewCommento(commento);
                    } else {
                        updateCommento(commento);
                    }
                });
        priorityService.findAll().stream()
                .forEach(priority -> {
                    if (null == priority.getTrelloId()) {
                        postNewPriority(priority);
                    } else {
                        updatePriority(priority);
                    }
                });
    }

    public void updatePriority(Priority priority) {
        Gson gson = new Gson();
        String labelJson = gson.toJson(priority.toTrelloEntity());

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        HttpResponse<String> response = Unirest.put("https://api.trello.com/1/labels/" +
                        priority.getTrelloId() )
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .body(labelJson)
                .asString();
    }

    public void postNewPriority(Priority priority) {

        Gson gson = new Gson();
        System.out.println(priority.toTrelloEntity());
        String boardId = "652d5727a3301d21fa288a27";
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();

        String labelJson = gson.toJson(priority.toTrelloEntity());
        System.out.println(labelJson);

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/labels")
                .headers(headers)
                .queryString("idBoard", boardId)
                .queryString("key", key)
                .queryString("token", token)
                .body(labelJson)
                .asJson();

        TrelloLabel label = gson.fromJson(response.getBody().toPrettyString(), TrelloLabel.class);
        priority.setTrelloId(label.getId());
        priorityService.update(priority);

        System.out.println(response);
    }

    private void updateCommento(Commento commento) {
        Gson gson = new Gson();
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        HttpResponse<JsonNode> response = Unirest.put("https://api.trello.com/1/actions/" +
                        commento.getTrelloId())
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .queryString("text", commento.getCommento())
                .asJson();
    }

    private void postNewCommento(Commento commento) {
        Gson gson = new Gson();
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        String idCard = commento.getTask().getTrelloId();

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards/" +
                        idCard +
                        "/actions/comments")
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .queryString("text", commento.getCommento())
                .asJson();
        TrelloAction trelloAction = gson.fromJson(response.getBody().toPrettyString(), TrelloAction.class);
        commento.setTrelloId(trelloAction.getId());
        commentoService.update(commento);
    }

    private ListTrello postNewTabella(Tabella tabella) {
        Gson gson = new Gson();
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        String check = "missing board id";
        String idBoard = tabellaService.findAll().stream()
                .map(Tabella::getTrelloBoardId)
                .filter(id -> id != null)
                .findFirst().orElse(check);
        if (idBoard.equals(check)) {
            throw new BoardIdMissingException();
        }
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/lists")
                .queryString("name", tabella.getNome())
                .queryString("idBoard", idBoard)
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

        System.out.println(response.getBody().toPrettyString());
        ListTrello listTrello = gson.fromJson(response.getBody().toPrettyString(), ListTrello.class);
        tabella.setTrelloId(listTrello.getId());
        tabella.setTrelloBoardId(listTrello.getIdBoard());
        tabellaService.update(tabella);
        return listTrello;
    }

    private ListTrello updateTabella(Tabella tabella) {
        Gson gson = new Gson();
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();

        HttpResponse<JsonNode> response = Unirest.put("https://api.trello.com/1/lists/" +
                        tabella.getTrelloId())
                .queryString("key", key)
                .queryString("token", token)
                .queryString("name", tabella.getNome())
                .asJson();

        System.out.println(response.getBody().toPrettyString());
        ListTrello listTrello = gson.fromJson(response.getBody().toPrettyString(), ListTrello.class);
        return listTrello;
    }

    public Card postNewCard(Task task) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();

        String cardJson = gson.toJson(task.toTrelloEntity());
        System.out.println(cardJson);

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        String listId = task.getTabella().getTrelloId();

        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards/")
                .headers(headers)
                .queryString("idList", listId)
                .queryString("key", key)
                .queryString("token", token)
                .body(cardJson)
                .asJson();

        System.out.println(response.getBody().toPrettyString());
        Card card = gson.fromJson(response.getBody().toPrettyString(), Card.class);
        task.setTrelloId(card.getId());
        task.setTrelloListId(card.getIdList());
        taskService.update(task);
        return card;
    }

    private Card updateCard(Task task) {
        Gson gson = new Gson();
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        String idCard = task.getTrelloId();

        Card cardToUpdate = task.toTrelloEntity();
        cardToUpdate.setIdList(task.getTabella().getTrelloId());
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        HttpResponse<JsonNode> response = Unirest.put("https://api.trello.com/1/cards/" +
                        idCard)
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .body(gson.toJson(cardToUpdate))
                .asJson();

        System.out.println(response.getBody().toPrettyString());
        return gson.fromJson(response.getBody().toPrettyString(), Card.class);
    }

    //TODO tidy this mess up and expose only the method to synchronize in a separate controller after you finish implementing the synchronization of checklist and checkmark

}
