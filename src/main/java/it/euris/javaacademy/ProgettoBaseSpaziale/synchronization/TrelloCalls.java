package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.Card;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ListTrello;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;

@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/synchronize_verbose")
public class TrelloCalls {

    ApiKeyService apiKeyService;

    Gson gson = new Gson();
    String key = "656d5bde047c3ac9c66eae4c33aa9230";
    String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
    String idList = "652d5727a3301d21fa288a28";

//    ExclusionStrategy strategy = new ExclusionStrategy() {
//        @Override
//        public boolean shouldSkipField(FieldAttributes field) {
//            if (field.getDeclaringClass() == ListTrello.class && field.getName().equals("other")) {
//                return true;
//            }
//            if (field.getDeclaringClass() == ListTrello.class && field.getName().equals("otherVerboseInfo")) {
//                return true;
//            }
//            return false;
//        }
//
//        @Override
//        public boolean shouldSkipClass(Class<?> clazz) {
//            return false;
//        }
//    };

    //        String key = apiKeyService.findMostRecent().getKey();
//        String token = apiKeyService.findMostRecent().getToken();
    public void trelloGetCardsOnABoard() {
        String boardId = "652d5727a3301d21fa288a27";

        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" +
                        boardId +
                        "/cards")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();


        System.out.println(response.getBody().toPrettyString());
    }

    public void postNewCard() {
        String boardId = "652d5727a3301d21fa288a27";
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


    public void trelloListFromJson() {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/lists/" + idList
                )
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

        ListTrello list = gson.fromJson(response.getBody().toPrettyString(), ListTrello.class);
        System.out.println(list.toString());
    }

    public void trelloListFromJsonList() {

       String boardId = "652d5727a3301d21fa288a27";

        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" +
                        boardId +
                        "/lists")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

        System.out.println(response.getBody().toPrettyString());

        List<ListTrello> listTrellos = getList(response.getBody().toString(), ListTrello.class);
        System.out.println(listTrellos.toString());
    }


    public void allTrelloListFromJsonList() {

        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<JsonNode> response = Unirest.get("https://api.trello.com/1/boards/" +
                        idBoard +
                        "/lists")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();

        System.out.println(response.getBody().toPrettyString());

        List<ListTrello> listTrellos = getList(response.getBody().toString(), ListTrello.class);
        System.out.println(listTrellos.toString());
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

        System.out.println(response.getBody().toPrettyString());

        List<ListTrello> listTrellos = getList(response.getBody().toString(), ListTrello.class);
        return listTrellos;
    }

    public void cardsFromJsonListId(String boardId) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedTimeAdapter())
                .create();

//        String boardId = "652d5727a3301d21fa288a28";
        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/lists/" +
                        boardId +
                        "/cards")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        System.out.println(response.getBody());

        List<Card> cards = getList(response.getBody(), Card.class);
        System.out.println(cards.toString());
    }

    public List<Card> cardsFromJsonListIdWithReturn(String boardId) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedTimeAdapter())
                .create();

        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/lists/" +
                        boardId +
                        "/cards")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        List<Card> cards = getList(response.getBody(), Card.class);
        return cards;
    }

    public List<ListTrello> trelloListFromJsonListWithReturn() {

        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/boards/" +
                        idBoard +
                        "/lists")
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        System.out.println(response.getBody());

        List<ListTrello> listTrellos = getList(response.getBody(), ListTrello.class);
        return listTrellos;
    }


    public static void main(String args[]) throws Exception {
        TrelloCalls client = new TrelloCalls();
//        client.trelloListFromJson();
//        client.trelloListFromJsonList();
//        client.cardsFromJsonListId();

        List<Card> allCards = client.allTrelloListFromJsonListWithReturn().stream()
                .map(ListTrello::getId)
                .map(stringId -> client.cardsFromJsonListIdWithReturn(stringId))
                .flatMap(Collection::stream)
                .toList();

        System.out.println(allCards);
    }
}


