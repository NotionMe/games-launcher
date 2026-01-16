package ua.notion.controllers;

import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.lang.System.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
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
import ua.notion.services.RemoveGameService;
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

  private final RemoveGameService removeGameService = new RemoveGameService();

  private static final Logger LOGGER = System.getLogger(RemoveGameController.class.getName());

  @FXML
  private void initialize() {
    rootPane.getStylesheets()
        .addAll(getClass().getResource(UI.REMOVE_GAME_POPUP_CSS).toExternalForm());

    removePrefixVisible();
    windowHandler = new WindowHandler(rootPane);

    removeFromDiskCb.selectedProperty().addListener(observable -> {
      if (removeFromDiskCb.isSelected()) {
        removeFromLauncherCb.setSelected(true);
        removeFromLauncherCb.setDisable(true);
      } else {
        removeFromLauncherCb.setDisable(false);
      }
    });
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

  // FIXME TO LARGE METHOD!!!
  @FXML
  private void onYesButtonPressed() {
    LOGGER.log(INFO, "Yes button pressed!");

    boolean deleteFromLauncher = removeFromLauncherCb.isSelected();
    boolean deleteFromDisk = removeFromDiskCb.isSelected();
    boolean deletePrefix = removePrefixCb.isSelected();

    if (!deleteFromLauncher && !deleteFromDisk && !deletePrefix) {
      LOGGER.log(WARNING, "Nothing selected for removal.");
      return;
    }

    if (deleteFromDisk) {
      Path folderPath = Path.of(currentGame.targetPath()).getParent();
      if (folderPath != null && !folderPath.toString().isBlank()) {
        Optional<ButtonType> result = showConfirmationAlert(
            "Delete this Game Files?",
            "This will permanently delete the folder: " + folderPath);

        if (result.isEmpty() || result.get() != ButtonType.OK) {
          LOGGER.log(INFO, "Deletion canceled by user.");
          return;
        }
      }
      deleteFromLauncher = true;
    }

    if (deletePrefix) {
      String pfx = currentGame.pfx();
      if (pfx != null && !pfx.isBlank() && Files.exists(Path.of(pfx))) {
        Optional<ButtonType> result = showConfirmationAlert(
            "Delete Wine Prefix?",
            "This will remove all Wine settings and saves for this game at: " + pfx);

        if (result.isEmpty() || result.get() != ButtonType.OK) {
          LOGGER.log(INFO, "Prefix deletion canceled by user.");
          return;
        }
      }
    }

    final boolean finalDeleteFromLauncher = deleteFromLauncher;

    yesButton.setDisable(true);
    noButton.setDisable(true);
    rootPane.setCursor(javafx.scene.Cursor.WAIT);

    CompletableFuture<Void> diskTask = deleteFromDisk
        ? removeFromDisk()
        : CompletableFuture.completedFuture(null);
    CompletableFuture<Void> prefixTask = deletePrefix
        ? removePrefix()
        : CompletableFuture.completedFuture(null);

    CompletableFuture.allOf(diskTask, prefixTask)
        .thenRun(() -> Platform.runLater(() -> {
          if (finalDeleteFromLauncher) {
            removeFromLauncher();
          }
          LOGGER.log(INFO, "All tasks finished. Closing window.");
          windowHandler.close(rootPane);
        }))
        .exceptionally(ex -> {
          Platform.runLater(() -> {

            LOGGER.log(System.Logger.Level.ERROR, "Error removing game files", ex);

            yesButton.setDisable(false);
            noButton.setDisable(false);
            rootPane.setCursor(javafx.scene.Cursor.DEFAULT);
          });
          return null;
        });
  }

  private void removeFromLauncher() {
    LOGGER.log(INFO, "Remove from launcher selected!");
    removeGameService.removeGameFromLibrary(currentGame);
  }

  private CompletableFuture<Void> removeFromDisk() {
    if (!removeFromDiskCb.isSelected()) {
      return CompletableFuture.completedFuture(null);
    }

    LOGGER.log(INFO, "Remove from disk selected!");
    Path folderPath = Path.of(currentGame.targetPath()).getParent();

    if (folderPath == null || folderPath.toString().isBlank()) {
      LOGGER.log(WARNING, "Directory not found! " + currentGame.targetPath());
      return CompletableFuture.completedFuture(null);
    }

    return removeGameService.deleteDirectoryAsync(folderPath, "Game folder");
  }

  private CompletableFuture<Void> removePrefix() {
    LOGGER.log(INFO, "Remove prefix selected!");
    String pfx = currentGame.pfx();

    if (!removePrefixCb.isSelected()) {
      return CompletableFuture.completedFuture(null);
    }

    if (pfx == null || pfx.isBlank()) {
      LOGGER.log(WARNING, "Prefix path is not set for game: " + currentGame.title());
      return CompletableFuture.completedFuture(null);
    }

    Path pfxPath = Path.of(pfx);
    if (!Files.exists(pfxPath)) {
      LOGGER.log(WARNING, "Prefix directory does not exist: " + pfx);
      return CompletableFuture.completedFuture(null);
    }

    return removeGameService.deleteDirectoryAsync(pfxPath, "Game prefix");
  }

  private void removePrefixVisible() {
    if (OsUtils.isLinux()) {
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
        getClass().getResource(UI.REMOVE_GAME_ALERT_CSS).toExternalForm());
    return alert.showAndWait();
  }
}