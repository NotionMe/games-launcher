package ua.notion.controllers;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import ua.notion.services.LauchingServise;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.Constants.Views;

public class MainMenuController {


  private double xOffset = 0;
  private double yOffset = 0;

  @FXML
  private ImageView backgroundImageView;

  private User user;

  private final UserData userData = new UserData();
  private final IconService iconService = new IconService();
  private final GameService gameService = new GameService(userData, iconService);

  @FXML
  private StackPane rootPane;
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
  @FXML
  private StackPane centerLayer;



  private Parent gameSelectView;

  public StackPane getCenterLayer() {
    return centerLayer;
  }

  public Parent getGameSelectView() {
    return gameSelectView;
  }

  public void setGameSelectView(Parent gameSelectView) {
    this.gameSelectView = gameSelectView;
  }

  @FXML
  protected void handleCloseAction(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  protected void handleMinAction(ActionEvent event) {
    Stage stage = (Stage) minButton.getScene().getWindow();
    stage.setIconified(true);
  }

  @FXML
  protected void handleFullAction(ActionEvent event) {
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
  protected void handlePressAction(MouseEvent event) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      xOffset = stage.getX() - event.getScreenX();
      yOffset = stage.getY() - event.getScreenY();
    }
  }

  @FXML
  protected void handleMovementAction(MouseEvent event) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      stage.setX(event.getScreenX() + xOffset);
      stage.setY(event.getScreenY() + yOffset);
    }
  }

  @FXML
  private void onAddGameButtonPressed(ActionEvent event) {

    // Stage stage = (Stage) rootPane.getScene().getWindow();

    // Optional<Game> game = gameService.addGameFromFile(user, stage);
    // game.ifPresent(g -> {
    // try {
    // gameService.createGameCard(g, cardContainer);
    // gameService.hidePanelVisible(centerDropPane);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // });
    if (gameSelectView == null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.GAME_SELECT));
        Parent root = loader.load();

        GameSelectController controller = loader.getController();
        controller.setMainMenuController(this);
        setGameSelectView(root);
        centerLayer.getChildren().add(root);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void onSettingsButtonPressed(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.SETTINGS_MENU));
      Parent settingsView = loader.load();

      Stage settingsStage = new Stage();
      settingsStage.initOwner(rootPane.getScene().getWindow());
      settingsStage.initModality(Modality.APPLICATION_MODAL); // Block main menu

      settingsStage.initStyle(StageStyle.TRANSPARENT);

      Scene scene = new Scene(settingsView);
      scene.setFill(Color.TRANSPARENT);

      settingsStage.setScene(scene);
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
    rootPane.getStylesheets().addAll(getClass().getResource(UI.BASE_CSS).toExternalForm(),
        getClass().getResource(UI.MAIN_MENU_CSS).toExternalForm());

    user = userData.read();

    // Load game cards
    gameService.loadGameCards(user, centerDropPane, cardContainer);

    // init side-drawer fxml
    try {
      FXMLLoader sideDrawer = new FXMLLoader(getClass().getResource(Views.SIDE_DRAWER));
      Parent drawerRoot = sideDrawer.load();
      SideDrawerController controller = sideDrawer.getController();
      SideDrawerController.setMainMenuController(this);

      rootPane.getChildren().add(drawerRoot);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public StackPane getRootPane() {
    return rootPane;
  }

  public void transitionToBackground(String imagePath) {
    try {
      if (imagePath == null) {
        restoreDefaultBackground();
        return;
      }

      backgroundImageView.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));

      FadeTransition ft = new FadeTransition(Duration.millis(200), backgroundImageView);
      ft.setFromValue(0.0);
      ft.setToValue(1.0);
      ft.play();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void restoreDefaultBackground() {
    FadeTransition ft = new FadeTransition(Duration.millis(300), backgroundImageView);
    ft.setFromValue(backgroundImageView.getOpacity());
    ft.setToValue(0.0);
    ft.play();
  }
}
