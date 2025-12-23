package ua.notion.controllers;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.services.GameService;
import ua.notion.services.IconService;
import ua.notion.utils.StageUtils;
import ua.notion.utils.WindowHandler;

public class MainMenuController {


  @FXML
  private StackPane centerLayer;

  private User user;

  private static final String BASE_CSS = "/css/base.css";
  private static final String MAIN_MENU_CSS = "/css/main-menu.css";


  private final UserData userData = new UserData();
  private final IconService iconService = new IconService();
  private final GameService gameService = new GameService(userData, iconService);

  @FXML
  private BorderPane rootPane;
  @FXML
  private Pane topPane;
  @FXML
  private AnchorPane centerDropPane;
  @FXML
  private AnchorPane dropFileInfo;
  @FXML
  private FlowPane cardContainer;

  @FXML
  private Button fullButton;
  @FXML
  private Button minButton;
  @FXML
  private Button closeButton;
  @FXML
  private Button addButton;
  @FXML
  private Button settingsButton;
  @FXML
  private Button supportButton;

  @FXML
  private HBox bottomContainer;

  @FXML
  private ScrollPane gamesScroll;

  private WindowHandler windowHandler;

  @FXML
  protected void handleCloseAction(ActionEvent event) {
    windowHandler.close((Node) event.getSource());
  }

  @FXML
  protected void handleMinAction(ActionEvent event) {
    windowHandler.minimize((Node) event.getSource());
  }

  @FXML
  protected void handleFullAction(ActionEvent event) {
    windowHandler.toggleFullscreen((Node) event.getSource());
  }

  @FXML
  protected void handlePressAction(MouseEvent event) {
    windowHandler.onPress(event);
  }

  @FXML
  protected void handleMovementAction(MouseEvent event) {
    windowHandler.onDrag(event);
  }

  @FXML
  private void onAddGameButtonPressed(ActionEvent event) {

    Stage stage = (Stage) rootPane.getScene().getWindow();

    Optional<Game> game = gameService.addGameFromFile(user, stage);
    game.ifPresent(g -> {
      try {
        gameService.createGameCard(g, cardContainer);
        gameService.hidePanelVisible(centerDropPane);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  @FXML
  private void onSettingsButtonPressed(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(
          "/ua/notion/settings-menu.fxml"));
      Parent settingsView = loader.load();

      Stage settingsStage = new Stage();
      settingsStage.initOwner(rootPane.getScene().getWindow());
      settingsStage.initModality(Modality.APPLICATION_MODAL); // Block main menu

      settingsStage.initStyle(StageStyle.TRANSPARENT);

      Scene scene = new Scene(settingsView);
      scene.setFill(Color.TRANSPARENT);

      settingsStage.setScene(scene);

      StageUtils.configureScreenSize(settingsStage);

      settingsStage.showAndWait();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void fileViewDragDropped(DragEvent event) {
    var files = gameService.extractArchiveFiles(event);

    gameService.createCardsOnFiles(event, user, cardContainer, centerDropPane);
    gameService.hidePanelVisible(dropFileInfo);
    if (!UserData.fileIsExists()) {
      gameService.showPanelVisible(centerDropPane);
    }

    if (!files.isEmpty() && files.get(0).exists()) {
      gameService.hidePanelVisible(centerDropPane);
    } else {
      gameService.showPanelVisible(centerDropPane); // if 'files' not 'rar','zip','exe'
    }
  }

  @FXML
  private void fileViewDragOver(DragEvent event) {
    if (event.getDragboard().hasFiles()) {
      event.acceptTransferModes(TransferMode.COPY);
    }
    gameService.showPanelVisible(dropFileInfo);
    gameService.hidePanelVisible(centerDropPane);

    event.consume();
  }

  @FXML
  private void onFileDragExited(DragEvent event) {
    gameService.hidePanelVisible(dropFileInfo);

    if (cardContainer.getChildren().isEmpty()) {
      gameService.showPanelVisible(centerDropPane);
    } else {
      gameService.hidePanelVisible(centerDropPane);
    }
    event.consume();
  }

  @FXML
  private void initialize() {
    this.windowHandler = new WindowHandler(rootPane);

    rootPane.getStylesheets().addAll(getClass().getResource(BASE_CSS).toExternalForm(),
        getClass().getResource(MAIN_MENU_CSS).toExternalForm());

    user = userData.read();
  }
}
