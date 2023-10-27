package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.enums.Priorita;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TabellaRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.repositoy.TaskRepository;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TaskService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.ExclusionStrategy;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Modifier;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;

@AllArgsConstructor
@RestController
@RequestMapping("/synchronizeToTrello")
@SpringBootApplication
public class LocalDBCalls {

    TaskRepository taskRepository;
    TabellaRepository tabellaRepository;
    TabellaService tabellaService;
    TaskService taskService;
    ApiKeyService apiKeyService;


    public List<Card> trelloGetCardsOnABoard() {
        String boardId = "652d5727a3301d21fa288a27";
        String key = "656d5bde047c3ac9c66eae4c33aa9230";
        String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
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

    @PutMapping("/synchronize")
    public void doSomething() {
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
    }

    @PutMapping("/something")
    public List<Tabella> doSomethingg() {
        return tabellaService.findAll();
    }

    private ListTrello postNewTabella(Tabella tabella) {
        Gson gson = new Gson();
        String key = "656d5bde047c3ac9c66eae4c33aa9230";
        String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/lists")
                .queryString("name", tabella.getNome())
                .queryString("idBoard", idBoard)
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

        System.out.println(response.getBody().toPrettyString());
        ListTrello listTrello =  gson.fromJson(response.getBody().toPrettyString(), ListTrello.class);
        tabella.setTrelloId(listTrello.getId());
        tabella.setTrelloBoardId(listTrello.getIdBoard());
        tabellaService.update(tabella);
        return listTrello;
    }

    private ListTrello updateTabella(Tabella tabella) {
        Gson gson = new Gson();
        String key = "656d5bde047c3ac9c66eae4c33aa9230";
        String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";

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
        String key = "656d5bde047c3ac9c66eae4c33aa9230";
        String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";

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
        Card card =  gson.fromJson(response.getBody().toPrettyString(), Card.class);
        task.setTrelloId(card.getId());
        task.setTrelloListId(card.getIdList());
        taskService.update(task);
        return card;
    }

    private Card updateCard(Task task) {
        Gson gson = new Gson();
        String key = "656d5bde047c3ac9c66eae4c33aa9230";
        String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
        String idCard = task.getTrelloId();

        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        HttpResponse<JsonNode> response = Unirest.put("https://api.trello.com/1/cards/" +
                        idCard)
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .body(gson.toJson(task.toTrelloEntity()))
                .asJson();

        System.out.println(response.getBody().toPrettyString());
        return gson.fromJson(response.getBody().toPrettyString(), Card.class);
    }


    public static void main(String[] args) {
//        LocalDBCalls client = new LocalDBCalls();

    }

    //TODO tidy this mess up and expose only the method to synchronize in a separate controller after you finish implementing the synchronization of checklist and checkmark

}
