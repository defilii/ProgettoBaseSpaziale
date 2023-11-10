package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyTokenOrUrl;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.GsonUtils.getList;
import static it.euris.javaacademy.ProgettoBaseSpaziale.utils.RestCallUtils.getJsonStringFromUrlGetCall;

@NoArgsConstructor
@AllArgsConstructor
@RestController
public class TrelloCalls {

    ApiKeyService apiKeyService;

    public List<ListTrello> allTrelloListFromJsonListWithReturn() throws InvalidKeyTokenOrUrl {
        String idBoard = "652d5727a3301d21fa288a27";
        String url = "https://api.trello.com/1/boards/" + idBoard + "/lists";
        String response = getJsonStringFromUrlGetCall(url, apiKeyService);

        List<ListTrello> listTrellos = getList(response, ListTrello.class);

        for (ListTrello listTrello : listTrellos) {
            cardsFromJsonListId(listTrello.getId());
        }

        return listTrellos;
    }

    public List<Card> cardsFromJsonListId(String listId) throws InvalidKeyTokenOrUrl {
        String url = "https://api.trello.com/1/lists/" + listId + "/cards";
        String response = getJsonStringFromUrlGetCall(url, apiKeyService);

        List<Card> cards = getList(response, Card.class);

        for (Card card : cards) {
            getChecklistsAndSetItToCard(card);
            getAllCommentsFromCard(card);
        }

        return cards;
    }

    private void getChecklistsAndSetItToCard(Card card) throws InvalidKeyTokenOrUrl {
        String url = "https://api.trello.com/1/cards/" + card.getId() + "/checklists";
        String response = getJsonStringFromUrlGetCall(url, apiKeyService);

        List<TrelloChecklist> checklists = getList(response, TrelloChecklist.class);
        for (TrelloChecklist trelloChecklist : checklists) {
            getCheckmarksAndSetItToChecklist(trelloChecklist);
        }
        card.setTrelloChecklists(checklists);
    }

    private void getCheckmarksAndSetItToChecklist(TrelloChecklist trelloChecklist) throws InvalidKeyTokenOrUrl {
        String url = "https://api.trello.com/1/checklists/" + trelloChecklist.getId() + "/checkItems";
        String response = getJsonStringFromUrlGetCall(url, apiKeyService);


        List<CheckItem> checkItems = getList(response, CheckItem.class);
        System.out.println(checkItems.toString());
        trelloChecklist.setCheckItems(checkItems);
    }

    public List<TrelloLabel> getAllTrelloLabels() throws InvalidKeyTokenOrUrl {
        String idBoard = "652d5727a3301d21fa288a27";
        String url = "https://api.trello.com/1/boards/" + idBoard + "/labels";
        String response = getJsonStringFromUrlGetCall(url, apiKeyService);

        List<TrelloLabel> trelloLabels = getList(response, TrelloLabel.class);
        return trelloLabels;
    }

    public List<Members> getAllMembers() throws InvalidKeyTokenOrUrl {
        String idBoard = "652d5727a3301d21fa288a27";
        String url = "https://api.trello.com/1/boards/" + idBoard + "/members";
        String response = getJsonStringFromUrlGetCall(url, apiKeyService);

        List<Members> members = getList(response, Members.class);
        return members;
    }

    public List<TrelloAction> getAllCommentsFromCard(Card card) throws InvalidKeyTokenOrUrl {
        String url = "https://api.trello.com/1/cards/" + card.getId() + "/actions";
        String response = getJsonStringFromUrlGetCall(url, apiKeyService);

        List<TrelloAction> trelloActions = getList(response, TrelloAction.class);
        if (!trelloActions.isEmpty() || null == trelloActions) {
            trelloActions.removeIf(trelloAction -> null == trelloAction.getData().getText());
        }
        card.setTrelloActions(trelloActions);
        return trelloActions;
    }

}


