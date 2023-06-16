package com.test.fetchers;

import com.test.entities.Game;
import com.test.entities.League;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class DataFetcher {
    public CompletableFuture<String> fetchSportsData() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://leon.bet/api-2/betline/sports?ctag=en-US&flags=urlv2"))
                .build();

        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public String fetchGameData(League league) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "https://leon.bet/api-2/betline/changes/all?ctag=en-US&vtag=9c2cd386-31e1-4ce9-a140-28e9b63a9300&league_id=" + league.getId() + "&hideClosed=true&flags=reg,urlv2,mm2,rrc,nodup";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String fetchMarketData(Game game) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "https://leon.bet/api-2/betline/event/all?ctag=en-US&eventId=" + game.getId() +
                "&flags=reg,urlv2,mm2,rrc,nodup,smg,outv2";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
