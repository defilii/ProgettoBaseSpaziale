package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.TabellaService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;

@NoArgsConstructor
@AllArgsConstructor
public class TrelloEntityListMaker {

    TabellaService tabellaService;
    
    String key = "656d5bde047c3ac9c66eae4c33aa9230";
    String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
    String idList = "652d5727a3301d21fa288a28";

    public List<Card> cardsFromJsonListIdWithReturn(String boardId) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedTimeAdapter())
                .create();

        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/lists/" +
                        boardId +
                        "/cards")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

//        System.out.println(response.getBody().toPrettyString());

        List<Card> cards = getList(response.getBody().toString(), Card.class);
        return cards;
    }

    public List<ListTrello> allTrelloListFromJsonListWithReturn() {

        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" +
                        idBoard +
                        "/lists")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

//        System.out.println(response.getBody().toPrettyString());

        List<ListTrello> listTrellos = getList(response.getBody().toString(), ListTrello.class);
        return listTrellos;
    }

    public static void main(String args[]) throws Exception {





        

    }



}
