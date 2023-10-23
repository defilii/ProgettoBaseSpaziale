package it.euris.javaacademy.ProgettoBaseSpaziale.synchronization;

import ch.qos.logback.core.testUtil.FileTestUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.core.util.Json;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Tabella;
import it.euris.javaacademy.ProgettoBaseSpaziale.entity.Task;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/synchronize")
public class TrelloCalls {

    ApiKeyService apiKeyService;

    Gson gson = new Gson();
    String key = "656d5bde047c3ac9c66eae4c33aa9230";
    String token = "ATTA27702686ff9d2e286aadb299d53c874f655dc93f653cb20c42ea2f2be5eb111399494FE0";
//    String key = apiKeyService.findMostRecent().getKey();
//    String token = apiKeyService.findMostRecent().getToken();
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

    public static void main(String args[]) throws Exception {
        TrelloCalls client = new TrelloCalls();
        client.trelloGetCardsOnABoard();
    }
}


