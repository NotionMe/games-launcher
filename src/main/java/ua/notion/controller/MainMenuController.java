package ua.notion.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
  @FXML
  private StackPane centerLayer;


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
    game.ifPresent(g -> {
      try {
        gameService.createGameCard(g, cardContainer);
      } catch (IOException e1) {
      }
    });
  }

  @FXML
  private void fileViewDragDropped(DragEvent event) throws IOException {
    
    System.out.println(event.getDragboard().getFiles());



    // if (db.hasFiles()) {
    // File file = db.getFiles().get(0);

    // if (file.isFile()) {
    // Optional<Game> game = gameService.addGameFromArchive(user, file);
    // game.ifPresent(g -> {
    // try {
    // gameService.createGameCard(g, cardContainer);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // });
    // centerDropPane.setVisible(false);
    // System.out.println("da");
    // System.out.println("file: " + file);
    // event.setDropCompleted(true);
    // } else {
    // centerDropPane.setVisible(true);
    // System.out.println("file: " + file);
    // System.out.println("nea - not a file");
    // event.setDropCompleted(false);
    // }
    // } else {
    // centerDropPane.setVisible(true);
    // System.out.println("nea - no files");
    // event.setDropCompleted(false);
    // }

    event.consume();
  }

  @FXML
  private void fileViewDragOver(DragEvent event) {
    if (event.getDragboard().hasFiles()) {
      event.acceptTransferModes(TransferMode.COPY);
    }
    System.out.println(event.getDragboard().getFiles());
    event.consume();
  }

  @FXML
  private void initialize() {
    rootPane.getStylesheets().add(getClass().getResource(PATH_CSS).toExternalForm());

    user = userData.read();
  }
}
