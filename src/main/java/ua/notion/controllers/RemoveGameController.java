package ua.notion.controllers;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.awt.Desktop;
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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    windowHandler.close(rootPane);
  }

  @FXML
  private void onYesButtonPressed() {
    if (removeFromLauncherCb.isSelected()) {
      removeFromLauncher();
    }
    if (removePrefixCb.isSelected()) {
      System.out.println("Select prefix cb");
      removePrefix();
    }
    if (removeFromDiskCb.isSelected()) {
      removeFromDisk();
    }
    System.out.println("YES BUTTON PRESSED!");
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
    Path folderPath = Path.of(currentGame.targetPath()).getParent();

    if (folderPath == null || folderPath.toString().isBlank()) {
      LOGGER.log(WARNING, "Directory not found! " + currentGame.targetPath());
      return;
    }
    Alert alert = new Alert(AlertType.CONFIRMATION);
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isEmpty() || result.get() != ButtonType.OK) {
      LOGGER.log(INFO, "Canceled to remove game from disk");
      return;
    }
    LOGGER.log(INFO, "Deleting game folder: " + folderPath);
    CompletableFuture.runAsync(() -> {
      try {
        try (var walk = Files.walk(folderPath)) {
          walk.sorted(Comparator.reverseOrder())
              .map(Path::toFile)
              .forEach(File::delete);
        }
      } catch (IOException e) {
        LOGGER.log(ERROR, "Cannot delete folder! " + e.getMessage());
      }
    }).thenRun(() ->
        LOGGER.log(INFO, "Game folder deleted successfully."));
  }

  // todo, доробити, поки що чисто беремо шлях з current game, що вже результат :)
  private void removePrefix() {
    String pfx = currentGame.pfx();
    if (pfx == null || pfx.isBlank()) {
      LOGGER.log(WARNING, "Prefix path is not set for game: " + currentGame.title());
      return;
    }
    System.out.println("NOT NULL!)");
  }

  private void removePrefixVisible() {
    if (OsUtils.isWindows()) { // FIXME don`t forget switch isWindows to isLinux
      removePrefixCb.setVisible(true);
      removePrefixCb.setManaged(true);
    }
  }
}