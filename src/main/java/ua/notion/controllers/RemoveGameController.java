package ua.notion.controllers;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.services.GameService;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.OsUtils;
import ua.notion.utils.WindowHandler;

public class RemoveGameController {

  @FXML
  private StackPane rootPane;
  @FXML
  private AnchorPane topPane;

  @FXML
  private Label removeGameLabel;

  @FXML
  private Button noButton;
  @FXML
  private Button yesButton;

  @FXML
  private CheckBox removeFromLauncherCb;
  @FXML
  private CheckBox removePrefixCb;
  @FXML
  private CheckBox removeFromDiskCb;

  private WindowHandler windowHandler;

  private Game currentGame;

  private final UserRepository userData = new UserData();

  private static final Logger LOGGER = System.getLogger(RemoveGameController.class.getName());

  @FXML
  private void initialize() {
    rootPane.getStylesheets()
        .addAll(getClass().getResource(UI.REMOVE_GAME_POPUP_CSS).toExternalForm());

    removePrefixVisible();
    windowHandler = new WindowHandler(rootPane);
  }

  public void setCurrentGame(Game currentGame) {
    this.currentGame = currentGame;

    if (currentGame != null) {
      removeGameLabel.setText("Remove " + currentGame.title() + " from launcher");
    }
  }

  @FXML
  private void handleMovementAction(MouseEvent event) {
    windowHandler.onDrag(event);
  }

  @FXML
  private void handlePressAction(MouseEvent event) {
    windowHandler.onPress(event);
  }

  @FXML
  private void onNoButtonPressed() {
    LOGGER.log(INFO, "No button pressed!");
    windowHandler.close(rootPane);
  }

  @FXML
  private void onYesButtonPressed() {
    LOGGER.log(INFO, "Yes button pressed!");
    if (!removeFromLauncherCb.isSelected() && !removePrefixCb.isSelected() && !removeFromDiskCb.isSelected()) {
      LOGGER.log(WARNING, "Nothing selected for removal.");
      return;
    }

    if (removeFromLauncherCb.isSelected()) {
      LOGGER.log(INFO, "Remove from launcher selected!");
      removeFromLauncher();
    }
    if (removePrefixCb.isSelected()) {
      LOGGER.log(INFO, "Remove prefix selected!");
      removePrefix();
    }
    if (removeFromDiskCb.isSelected()) {
      LOGGER.log(INFO, "Remove from disk selected!");
      removeFromDisk();
    }
    windowHandler.close(rootPane);
  }

  private void removeFromLauncher() {
    if (currentGame == null) {
      LOGGER.log(WARNING, "Game not exists!");
      return;
    }
    User user = userData.findAll();
    user.removeGame(currentGame);

    userData.save(user);
    GameService.refreshLibraryInMenu();

    LOGGER.log(INFO, "Game: " + currentGame.title() + " removed!");
  }

  private void removeFromDisk() {
    if (currentGame == null) {
      LOGGER.log(WARNING, "Game not exists!");
      return;
    }
    Path folderPath = Path.of(currentGame.targetPath()).getParent();

    if (folderPath == null || folderPath.toString().isBlank()) {
      LOGGER.log(WARNING, "Directory not found! " + currentGame.targetPath());
      return;
    }

    Optional<ButtonType> result = showConfirmationAlert(
        "Delete this Game Files?",
        "This will permanently delete the folder: " + folderPath
    );

    if (result.isEmpty() || result.get() != ButtonType.OK) {
      LOGGER.log(INFO, "Canceled to remove game from disk");
      return;
    }
    deleteFolderAsync(folderPath, "Game folder");
  }

  private void removePrefix() {
    String pfx = currentGame.pfx();
    if (pfx == null || pfx.isBlank()) {
      LOGGER.log(WARNING, "Prefix path is not set for game: " + currentGame.title());
      return;
    }
    Path pfxPath = Path.of(pfx);
    if (!Files.exists(pfxPath)) {
      LOGGER.log(WARNING, "Prefix directory does not exist: " + pfx);
      return;
    }
    Optional<ButtonType> result = showConfirmationAlert(
        "Delete Wine Prefix?",
        "This will remove all Wine settings and saves for this game at: " + pfxPath
    );

    if (result.isEmpty() || result.get() != ButtonType.OK) {
      LOGGER.log(INFO, "Canceled to remove game from disk");
      return;
    }
    deleteFolderAsync(pfxPath, "Game prefix");
  }

  private void removePrefixVisible() {
    if (OsUtils.isWindows()) { // FIXME don`t forget switch isWindows to isLinux
      removePrefixCb.setVisible(true);
      removePrefixCb.setManaged(true);
    }
  }

  private Optional<ButtonType> showConfirmationAlert(String header, String content) {
    Alert alert = new Alert(AlertType.CONFIRMATION);

    alert.setHeaderText(header);
    alert.setContentText(content);

    alert.initStyle(StageStyle.TRANSPARENT);
    alert.setGraphic(null);

    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.setPrefSize(300, 170);
    dialogPane.getStyleClass().add("remove-game-alert");

    dialogPane.getScene().setFill(Color.TRANSPARENT);

    dialogPane.getStylesheets().add(
        getClass().getResource(UI.REMOVE_GAME_ALERT_CSS).toExternalForm()
    );
    return alert.showAndWait();
  }

  private void deleteFolderAsync(Path folderPath, String description) {
    LOGGER.log(INFO, "Starting async deletion of " + description + ": " + folderPath);

    CompletableFuture.runAsync(() -> {
      try (var walk = Files.walk(folderPath)) {
        walk.sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
      } catch (IOException e) {
        LOGGER.log(ERROR, "Failed to delete " + description + "! " + e.getMessage());
      }
    }).thenRun(() -> LOGGER.log(INFO, description + " deleted successfully."));
  }
}