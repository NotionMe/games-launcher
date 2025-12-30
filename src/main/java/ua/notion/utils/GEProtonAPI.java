package ua.notion.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.notion.data.dto.ProtonDTO;

public class GEProtonAPI {
    HttpClient client = HttpClient.newHttpClient();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File file = new File("proton.json");

    public List<ProtonDTO> getProtonVersion() {
        try {
            String url = "https://api.github.com/repos/GloriousEggroll/proton-ge-custom/releases";
            String responseBody = makeRequest(url);
            JsonArray rootArray = JsonParser.parseString(responseBody).getAsJsonArray();
            List<ProtonDTO> resultList = new ArrayList<>();

            for (JsonElement releaseEl : rootArray) {
                JsonObject release = releaseEl.getAsJsonObject();
                String tagName = release.get("tag_name").getAsString();
                JsonArray assets = release.get("assets").getAsJsonArray();

                for (JsonElement assetEl : assets) {
                    JsonObject asset = assetEl.getAsJsonObject();
                    String fileName = asset.get("name").getAsString();

                    if (fileName.endsWith(".tar.gz") || fileName.endsWith(".tar.zst")) {
                        JsonObject item = new JsonObject();
                        item.addProperty("version", tagName);
                        item.addProperty("name", fileName);
                        item.addProperty("url", asset.get("browser_download_url").getAsString());

                        ProtonDTO dto = gson.fromJson(item, ProtonDTO.class);
                        resultList.add(dto);
                    }
                }
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }



    private String makeRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "Java-Games-Launcher").GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Unexpected response code: " + response.statusCode() + " Body: "
                    + response.body());
        }

        return response.body();
    }
}
