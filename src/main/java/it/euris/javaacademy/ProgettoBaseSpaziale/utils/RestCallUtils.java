package it.euris.javaacademy.ProgettoBaseSpaziale.utils;

import it.euris.javaacademy.ProgettoBaseSpaziale.exceptions.InvalidKeyTokenOrUrl;
import it.euris.javaacademy.ProgettoBaseSpaziale.service.ApiKeyService;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;

import java.util.HashMap;
import java.util.Map;

public class RestCallUtils {

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

        return response.getBody().toPrettyString();
    }
}
