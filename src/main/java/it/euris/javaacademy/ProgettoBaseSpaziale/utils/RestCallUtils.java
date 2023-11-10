package it.euris.javaacademy.ProgettoBaseSpaziale.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyTokenOrUrl;
import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.RejectWebTrafficExceeded;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import it.euris.javaacademy.ProgettoBaseSpaziale.trello.ErrorBody;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;

import java.util.HashMap;
import java.util.Map;

public class RestCallUtils {

    private static void checkForErrors(HttpResponse<JsonNode> response) {
        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(response.getBody().toPrettyString());
        if (jsonElement.isJsonObject()) {
            ErrorBody errorBody = gson.fromJson(jsonElement, ErrorBody.class);
            if (null != errorBody.getMessage() && errorBody.getError().contains("REJECT_WEB_TRAFFIC_EXCEEDED")) {
                try {
                    throw new RejectWebTrafficExceeded();
                } catch (RejectWebTrafficExceeded e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String getJsonStringFromUrlGetCall(String url, ApiKeyService apiKeyService) throws InvalidKeyTokenOrUrl {
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("Accept", "application/json")
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        if (null == response.getBody()) {
            throw new InvalidKeyTokenOrUrl();
        }
        checkForErrors(response);

        System.out.println(response.getBody().toPrettyString());
        return response.getBody().toPrettyString();
    }

    public static String postJsonString(String url, String json, ApiKeyService apiKeyService) throws InvalidKeyTokenOrUrl {
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        HttpResponse<JsonNode> response = Unirest.post(url)
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .body(json == null ? "" : json)
                .asJson();
        if (null == response.getBody()) {
            throw new InvalidKeyTokenOrUrl();
        }

        checkForErrors(response);

        return response.getBody().toPrettyString();
    }

    public static String putJsonString(String url, String json, ApiKeyService apiKeyService) throws InvalidKeyTokenOrUrl {
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        System.out.println(url);
        HttpResponse<JsonNode> response = Unirest.put(url)
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .body(json == null ? "" : json)
                .asJson();

        if (null == response.getBody()) {
            throw new InvalidKeyTokenOrUrl();
        }

        checkForErrors(response);

        return response.getBody().toPrettyString();
    }

    public static String deleteWithRestCall(String url, ApiKeyService apiKeyService) throws InvalidKeyTokenOrUrl {
        String key = apiKeyService.findMostRecent().getKey();
        String token = apiKeyService.findMostRecent().getToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", "application/json");
        headers.put("content-type", "application/json");

        HttpResponse<JsonNode> response = Unirest.delete(url)
                .headers(headers)
                .queryString("key", key)
                .queryString("token", token)
                .asJson();
        if (null == response.getBody()) {
            throw new InvalidKeyTokenOrUrl();
        }

        checkForErrors(response);

        return response.getBody().toPrettyString();
    }
}
