package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import it.euris.javaacademy.ProgettoBaseSpaziale.utils.ExclusionStrategy;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Modifier;
import java.time.ZonedDateTime;
import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;

@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/synchronize_verbose")
public class TrelloCalls {

    ApiKeyService apiKeyService;

    ExclusionStrategy strategy = new ExclusionStrategy();

    Gson gson = new GsonBuilder().setExclusionStrategies(strategy).create();
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

    public void trelloListFromJsonList() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .create();
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

    public List<Card> cardsFromJsonListId(String boardId) {
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

//        System.out.println(response.getBody());

        List<Card> cards = getList(response.getBody(), Card.class);

        for (Card card: cards) {
            getChecklistsAndSetItToCard(card);
        }


//        System.out.println(response.getBody());

        System.out.println(cards.toString());
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
//        System.out.println(checklistResponse.getBody());

        List<TrelloChecklist> checklists = getList(checklistResponse.getBody(), TrelloChecklist.class);
        for (TrelloChecklist trelloChecklist: checklists) {
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

    public List<Members> membersFromJsonListWithReturn() {

        String idBoard = "652d5727a3301d21fa288a27";
        HttpResponse<String> response = Unirest.get("https://api.trello.com/1/boards/" +
                        idBoard +
                        "/members")
                .queryString("key", key)
                .queryString("token", token)
                .asString();

        System.out.println(response.getBody());

        List<Members> membersList = getList(response.getBody(), Members.class);
        return membersList;
    }


    public static void main(String args[]) throws Exception {
        TrelloCalls client = new TrelloCalls();
//        client.trelloListFromJson();
//        client.trelloListFromJsonList();
//        client.cardsFromJsonListId("652d5727a3301d21fa288a28");

        System.out.println(client.cardsFromJsonListId("6531489c31b7bdbe440ca06d")
                .stream().map(Card::toLocalEntity).toList()
                );
;




    }
}


