package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import it.euris.javaacademy.ProgettoBaseSpaziale.service.impl.ApiKeyServiceImpl;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;


public class TrelloCalls {

    ApiKeyServiceImpl apiKeyService;
    String boardId = "652d5727a3301d21fa288a27";
    String key = "656d5bde047c3ac9c66eae4c33aa9230";
    String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
    public void trelloGetCardsOnABoard() {

        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" +
                        boardId +
                        "/cards")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

        System.out.println(response.getBody());
    }

    public void postNewCard() {
        HttpResponse<JsonNode> response = Unirest.post("https://api.trello.com/1/cards")
                .header("Accept", "application/json")
                .queryString("idList", "6531489c31b7bdbe440ca06d")
                .queryString("key", key)
                .queryString("token", token)
                .field("name", "card inserita tramite unirest")
                .field("desc", "test descrizione")
                .asJson();

        System.out.println(response.getBody());
    }

    public static void main(String args[]) throws Exception {
        TrelloCalls client = new TrelloCalls();
        client.postNewCard();
    }
}


