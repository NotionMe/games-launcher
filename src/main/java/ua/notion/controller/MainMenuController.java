package ua.notion.controller;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.services.GameService;
import ua.notion.services.IconService;

public class MainMenuController {

  private double xOffset = 0;
  private double yOffset = 0;

  private User user;

  private final static String PATH_CSS = "/css/styles.css";

  private final UserData userData = new UserData();
  private final IconService iconService = new IconService();
  private final GameService gameService = new GameService(userData, iconService);

  @FXML
  private BorderPane rootPane;
  @FXML
  private Button buttonFull;
  @FXML
  private Button buttonMin;
  @FXML
  private Button buttonClose;
  @FXML
  private Pane topPane;
  @FXML
  private Button addButton;
  @FXML
  private Button settingsButton;
  @FXML
  private Button supportButton;
  @FXML
  private HBox bottomHboxStyle;
  @FXML
  private AnchorPane centerDropPane;
  @FXML
  private FlowPane cardContainer;
  @FXML
  private StackPane card;
  @FXML
  private ImageView cover;
  @FXML
  private ImageView icon;
  @FXML
  private Label title;
  @FXML
  private ScrollPane gamesScroll;

  @FXML
  protected void handleCloseAction(ActionEvent e) {
    Stage stage = (Stage) buttonClose.getScene().getWindow();
    stage.close();
  }

  @FXML
  protected void handleMinAction(ActionEvent e) {
    Stage stage = (Stage) buttonMin.getScene().getWindow();
    stage.setIconified(true);
  }

  @FXML
  protected void handleFullAction(ActionEvent e) {
    Stage stage = (Stage) rootPane.getScene().getWindow();
    stage.setFullScreenExitHint("");

    if (stage.isFullScreen()) {
      stage.setFullScreen(false);
      rootPane.getStyleClass().remove("fullscreen");
    } else {
      stage.setFullScreen(true);
      rootPane.getStyleClass().add("fullscreen");
    }
  }

  @FXML
  protected void handlePressAction(MouseEvent e) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      xOffset = stage.getX() - e.getScreenX();
      yOffset = stage.getY() - e.getScreenY();
    }
  }

  @FXML
  protected void handleMovementAction(MouseEvent e) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      stage.setX(e.getScreenX() + xOffset);
      stage.setY(e.getScreenY() + yOffset);
    }
  }

  @FXML
  private void onAddGameButtonPressed(ActionEvent e) throws IOException {

    Stage stage = (Stage) rootPane.getScene().getWindow();

    Optional<Game> game = gameService.addGameFromFile(user, stage);

    if (game.isPresent()) {
      gameService.createGameCard(game.get(), cardContainer);
    }
  }

  @FXML
  private void initialize() {
    rootPane.getStylesheets().add(getClass().getResource(PATH_CSS).toExternalForm());

    user = userData.read();
  }
}
