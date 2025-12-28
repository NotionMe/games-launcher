package ua.notion.controllers.settings;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ua.notion.services.GameService;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.GEProtonAPI;
import ua.notion.data.ProtonRepository;
import ua.notion.components.Proton;
import ua.notion.data.ProtonImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class ProtonsPageController {

  @FXML
  private StackPane rootPane;

  @FXML
  private ChoiceBox<String> protonsChoiceBox;

  @FXML
  private VBox protonsContainer;

  private final GEProtonAPI protonAPI = new GEProtonAPI();
  private final ProtonRepository protonRepository = new ProtonImpl();
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public void initialize() {
    rootPane.getStylesheets().addAll(getClass().getResource(UI.PROTONS_PAGE_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm());

    addProtonCheckBox();
    loadProtonList();
  }

  private void loadProtonList() {
    protonsContainer.getChildren().clear();

    File[] installedFiles = GameService.getProtonVersionHost(Data.PROTON_PATH.toString());
    List<String> installedNames =
        (installedFiles != null) ? Arrays.stream(installedFiles).map(File::getName).toList()
            : List.of();

    CompletableFuture.runAsync(() -> {
      JsonArray availableProtons = protonAPI.getProtonVersion();
      if (availableProtons != null) {
        Platform.runLater(() -> {
          for (JsonElement el : availableProtons) {
            JsonObject proton = el.getAsJsonObject();
            String name = proton.get("version").getAsString();
            String fullName = proton.has("name") ? proton.get("name").getAsString() : "";
            String extension = fullName.endsWith(".tar.zst") ? ".tar.zst" : ".tar.gz";

            boolean isInstalled = installedNames.contains(name);
            addProtonItem(name, extension, isInstalled);
          }
        });
      }
    });
  }

  private void addProtonItem(String name, String extension, boolean isInstalled) {
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/ua/notion/settings/proton-item.fxml"));
      HBox item = loader.load();
      ProtonItemController controller = loader.getController();

      controller.setData(name, extension, isInstalled, () -> {
        if (isInstalled) {
          System.out.println("Delete logic for: " + name);
        } else {
          System.out.println("Download logic for: " + name);
        }
      });

      protonsContainer.getChildren().add(item);
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
  }

  private void addProtonCheckBox() {
    File[] files = GameService.getProtonVersionHost(Data.PROTON_PATH.toString());

    if (files != null && files.length > 0) {
      List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
      protonsChoiceBox.getItems().addAll(fileNames);
    }
    protonsChoiceBox.getSelectionModel().selectFirst();
  }
}
