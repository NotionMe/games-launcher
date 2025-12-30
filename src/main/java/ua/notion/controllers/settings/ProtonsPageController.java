package ua.notion.controllers.settings;

import java.lang.System.Logger;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.DEBUG;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ua.notion.services.GameService;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.Constants.Views;
import ua.notion.utils.GEProtonAPI;
import ua.notion.utils.PackageInstaller;
import ua.notion.data.ProtonRepository;
import ua.notion.data.mapper.DataMapper;
import ua.notion.components.Proton;
import ua.notion.data.ProtonImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import ua.notion.utils.ArchiveHelper;

public class ProtonsPageController {

  @FXML
  private StackPane rootPane;

  @FXML
  private ChoiceBox<String> protonsChoiceBox;

  @FXML
  private VBox protonsContainer;

  private static final Logger LOGGER = System.getLogger(ProtonsPageController.class.getName());

  private final GEProtonAPI protonAPI = new GEProtonAPI();
  private final PackageInstaller installer = new PackageInstaller();
  private final ProtonRepository protonRepository = new ProtonImpl();
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final File file = new File(Data.PROTON_PATH_JSON.toString());
  private Path protonPath = null;

  public void initialize() {
    rootPane.getStylesheets().addAll(getClass().getResource(UI.PROTONS_PAGE_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm());

    addProtonCheckBox();

    List<Proton> cachedProtons = protonRepository.findAll();
    if (!cachedProtons.isEmpty()) {
      displayProtons(cachedProtons);
    }
    checkNewProtons(cachedProtons);
  }

  private void addProtonItem(String name, String extension, boolean isInstalled, String urlProton) {
    LOGGER.log(DEBUG, "Adding proton item: {0}", name);
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.PROTON_ITEM));
      HBox item = loader.load();
      ProtonItemController controller = loader.getController();

      updateProtonItemUI(controller, name, extension, isInstalled, urlProton);
      protonsContainer.getChildren().add(item);
    } catch (IOException e) {
      LOGGER.log(System.Logger.Level.ERROR, "Failed to load proton item view: {0}", name, e);
    }
  }

  private void updateProtonItemUI(ProtonItemController controller, String name, String extension,
      boolean isInstalled, String url) {
    controller.setData(name, extension, isInstalled, () -> {
      if (isInstalled) {
        onDeleteProton(controller, name, extension, url);
        addProtonCheckBox();
      } else {
        onInstallProton(controller, name, extension, url);
        addProtonCheckBox();
      }
    });
  }

  private void onDeleteProton(ProtonItemController controller, String name, String extension,
      String url) {
    LOGGER.log(INFO, "Deleting proton: {0}", name);
    controller.setLoading(true);
    deleteProton(name).thenRun(() -> Platform.runLater(() -> {
      LOGGER.log(INFO, "Successfully deleted proton: {0}", name);
      controller.setLoading(false);
      updateProtonItemUI(controller, name, extension, false, url);
    })).exceptionally(ex -> {
      LOGGER.log(System.Logger.Level.ERROR, "Failed to delete proton: {0}", name, ex);
      Platform.runLater(() -> controller.setLoading(false));
      return null;
    });
  }

  private void onInstallProton(ProtonItemController controller, String name, String extension,
      String url) {
    LOGGER.log(INFO, "Installing proton: {0} from {1}", name, url);
    controller.setLoading(true);
    installProton(url, extension).thenRun(() -> Platform.runLater(() -> {
      LOGGER.log(INFO, "Successfully installed proton: {0}", name);
      controller.setLoading(false);
      updateProtonItemUI(controller, name, extension, true, url);
    })).exceptionally(ex -> {
      LOGGER.log(System.Logger.Level.ERROR, "Failed to install proton: {0}", name, ex);
      Platform.runLater(() -> controller.setLoading(false));
      return null;
    });
  }

  private CompletableFuture<Void> deleteProton(String nameSearching) {
    if (nameSearching == null) {
      return CompletableFuture.completedFuture(null);
    }

    return CompletableFuture.runAsync(() -> {
      String deleteDirectory = searchPath(nameSearching);
      if (deleteDirectory == null) {
        LOGGER.log(System.Logger.Level.WARNING, "Proton directory not found for deletion: {0}",
            nameSearching);
        return;
      }

      try {
        FileUtils.deleteDirectory(new File(deleteDirectory));
      } catch (IOException e) {
        LOGGER.log(System.Logger.Level.ERROR, "Failed to delete proton directory: {0}",
            deleteDirectory, e);
        throw new RuntimeException(e);
      }
    });
  }

  private String searchPath(String name) {
    File[] installedFiles = GameService.getProtonVersionHost(Data.PROTON_PATH.toString());

    if (installedFiles == null)
      return null;

    for (File file : installedFiles) {
      if (file.getName().toLowerCase().contains(name.toLowerCase())) {
        return file.getAbsolutePath();
      }
    }
    return null;
  }

  private CompletableFuture<Void> installProton(String urlProton, String format) {
    return CompletableFuture.runAsync(() -> {
      try {
        String cachePath = installer.getCachePath(urlProton, format);
        File cacheFile = new File(cachePath);

        if (!cacheFile.exists()) {
          installer.downloadUrl(urlProton, cachePath);
        }

        protonPath = ArchiveHelper.extract(cachePath, Data.PROTON_PATH.toString());
        ArchiveHelper.deleteArchive(cachePath);
      } catch (IOException | URISyntaxException e) {
        throw new RuntimeException(e);
      }
    });
  }


  private void displayProtons(List<Proton> protons) {
    protonsContainer.getChildren().clear();
    List<String> installedProtons = getInstalledProtonHost();
    for (Proton proton : protons) {
      String name = proton.version();
      String extension = proton.name().endsWith(".tar.zst") ? ".tar.zst" : ".tar.gz";
      String url = proton.downloadUrl();
      boolean isInstalled = installedProtons.contains(name);

      addProtonItem(name, extension, isInstalled, url);
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

  private List<String> getInstalledProtonHost() {

    File[] installedFiles = GameService.getProtonVersionHost(Data.PROTON_PATH.toString());

    return (installedFiles != null) ? Arrays.stream(installedFiles).map(File::getName).toList()
        : List.of();
  }

  private void checkNewProtons(List<Proton> cachedProtons) {
    CompletableFuture.supplyAsync(() -> protonAPI.getProtonVersion()).thenAccept(remoteProtons -> {
      if (remoteProtons == null || remoteProtons.isEmpty())
        return;

      boolean needsUpdate = cachedProtons.isEmpty()
          || !remoteProtons.get(0).version().equals(cachedProtons.get(0).version());
      if (needsUpdate) {
        System.out.println("New Proton version found! Updating JSON...");

        protonRepository.save(remoteProtons);

        List<Proton> updatedList = remoteProtons.stream().map(DataMapper::toEntity).toList();
        Platform.runLater(() -> displayProtons(updatedList));
      }
    });
  }
}
