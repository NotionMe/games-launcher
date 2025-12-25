package ua.notion.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SteamGridDB {
    private static final String BASE_URL = "https://www.steamgriddb.com/api/v2";
    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;

    public SteamGridDB(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public JsonObject getGridsByGameId(int gameId) throws IOException, InterruptedException {
        return makeRequest("/grids/game/" + gameId);
    }

    public JsonObject getLogosByGameId(int gameId) throws IOException, InterruptedException {
        return makeRequest("/logos/game/" + gameId);
    }

    public JsonObject getIconsByGameId(int gameId) throws IOException, InterruptedException {
        return makeRequest("/icons/game/" + gameId);
    }

    public JsonObject getLogosBySteamId(String steamId) throws IOException, InterruptedException {
        return makeRequest("/logos/steam/" + steamId);
    }

    public JsonObject getIconsBySteamId(String steamId) throws IOException, InterruptedException {
        return makeRequest("/icons/steam/" + steamId);
    }

    public JsonObject searchGames(String term) throws IOException, InterruptedException {
        return makeRequest("/search/autocomplete/" + term.replace(" ", "%20"));
    }

    public JsonObject getGridsBySteamId(String steamId) throws IOException, InterruptedException {
        return makeRequest("/grids/steam/" + steamId);
    }

    public String getFirstImageUrl(JsonObject response) {
        if (response != null && response.has("success") && response.get("success").getAsBoolean()) {
            JsonArray data = response.getAsJsonArray("data");
            if (data != null && data.size() > 0) {
                return data.get(0).getAsJsonObject().get("url").getAsString();
            }
        }
        return null;
    }

    public int getGameIdByName(String name) throws IOException, InterruptedException {
        JsonObject response = searchGames(name);
        if (response != null && response.has("success") && response.get("success").getAsBoolean()) {
            JsonArray data = response.getAsJsonArray("data");
            if (data != null && data.size() > 0) {
                return data.get(0).getAsJsonObject().get("id").getAsInt();
            }
        }
        return -1;
    }

    private JsonObject makeRequest(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + apiKey).GET().build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected response code: " + response.statusCode() + " Body: "
                    + response.body());
        }

        return gson.fromJson(response.body(), JsonObject.class);
    }
}
