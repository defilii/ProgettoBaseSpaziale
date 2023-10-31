package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;

@NoArgsConstructor
@AllArgsConstructor
@RestController
public class TrelloCalls {

    ApiKeyService apiKeyService;

    String key;
    String token;


    public TrelloCalls(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    private void updateKeys() {
        key = apiKeyService.findMostRecent().getKey();
        token = apiKeyService.findMostRecent().getToken();
    }

    public List<ListTrello> allTrelloListFromJsonListWithReturn() {
        updateKeys();
        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" +
                        idBoard +
                        "/lists")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

        List<ListTrello> listTrellos = getList(response.getBody().toString(), ListTrello.class);

        for (ListTrello listTrello : listTrellos) {
            cardsFromJsonListId(listTrello.getId());
        }

        return listTrellos;
    }

    public List<Card> cardsFromJsonListId(String listId) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedTimeAdapter())
                .create();

        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/lists/" +
                        listId +
                        "/cards")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        List<Card> cards = getList(response.getBody(), Card.class);

        for (Card card : cards) {
            getChecklistsAndSetItToCard(card);
        }

        System.out.println(cards.toString());

        return cards;
    }

    public List<Card> cardsFromJsonList() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedTimeAdapter())
                .create();

        String listId = "652d5727a3301d21fa288a28";
        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/lists/" +
                        listId +
                        "/cards")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        List<Card> cards = getList(response.getBody(), Card.class);

        for (Card card : cards) {
            getChecklistsAndSetItToCard(card);
        }

        System.out.println(cards.stream().map(Card::toLocalEntity).toList());

        return cards;
    }

    private void getChecklistsAndSetItToCard(Card card) {
        HttpResponse<String> checklistResponse = Unirest.get("https://api.trello.com/1/cards/" +
                        card.getId()
                        +
                        "/checklists")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        List<TrelloChecklist> checklists = getList(checklistResponse.getBody(), TrelloChecklist.class);
        for (TrelloChecklist trelloChecklist : checklists) {
            getCheckmarksAndSetItToChecklist(trelloChecklist);
        }
        card.setTrelloChecklists(checklists);
    }

    private void getCheckmarksAndSetItToChecklist(TrelloChecklist trelloChecklist) {
        HttpResponse<String> checkitemResponse = Unirest.get("https://api.trello.com/1/checklists/" +
                        trelloChecklist.getId()
                        +
                        "/checkItems")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        List<CheckItem> checkItems = getList(checkitemResponse.getBody(), CheckItem.class);
        trelloChecklist.setCheckItems(checkItems);
    }

    public List<TrelloLabel> getAllTrelloLabels() {
        String key ="656d5bde047c3ac9c66eae4c33aa9230";
        String token ="ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/boards/" +
                        idBoard +
                        "/labels")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        List<TrelloLabel> trelloLabels = getList(response.getBody(), TrelloLabel.class);
    return trelloLabels;
    }

    public List<Members> getAllMembers() {
        String key ="656d5bde047c3ac9c66eae4c33aa9230";
        String token ="ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/boards/" +
                        idBoard +
                        "/members")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        List<Members> members = getList(response.getBody(), Members.class);
        return members;
    }


    public static void main(String[] args) {
        TrelloCalls client = new TrelloCalls();
        System.out.println(client.getAllMembers().stream().map(Members::toLocalEntity).toList());;

    }

}


