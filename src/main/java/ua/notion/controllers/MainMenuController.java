package ua.notion.controllers;

import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.services.GameService;
import ua.notion.services.IconService;
import ua.notion.ui.fx.WindowHelper;
import javafx.scene.image.ImageView;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.Constants.Views;
import ua.notion.utils.StageUtils;
import ua.notion.utils.WindowHandler;

public class MainMenuController {

  private WindowHandler windowHandler;

  private User user;

  private final UserRepository userData = new UserData();
  private final IconService iconService = new IconService();
  private static final WindowHelper WINDOW_HELPER = new WindowHelper();
  private final GameService gameService = new GameService(userData, iconService);
  private final GameCardController gameCardController = new GameCardController();

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
  private AnchorPane bottomAnchorGroup;
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
  @FXML
  private ImageView backgroundImageView;

  private Parent gameSelectView;

  public StackPane getCenterLayer() {
    return centerLayer;
  }

  public Parent getGameSelectView() {
    return gameSelectView;
  }

  public FlowPane getCardContainer() {
    return cardContainer;
  }

  public AnchorPane getCenterDropPane() {
    return centerDropPane;
  }

  public ImageView getBackgroundImageView() {
    return backgroundImageView;
  }

  public void setGameSelectView(Parent gameSelectView) {
    this.gameSelectView = gameSelectView;
  }

  public AnchorPane getBottomAnchorGroup() {
    return bottomAnchorGroup;
  }

  public void setBottomAnchorGroup(AnchorPane bottomAnchorGroup) {
    this.bottomAnchorGroup = bottomAnchorGroup;
  }

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
    if (gameSelectView == null) {
      SideDrawerController.setMainMenuController(this);
      gameSelectView = (Parent) WINDOW_HELPER.navigateAdd(Views.GAME_SELECT);
      if (gameSelectView != null) {
        centerLayer.getChildren().add(gameSelectView);
        gameService.hidePanelVisible(bottomAnchorGroup);
      }
    }
  }

  @FXML
  private void onSettingsButtonPressed(ActionEvent event) {
    Parent settingsView = (Parent) WINDOW_HELPER.navigateAdd(Views.SETTINGS_MENU);

    Stage settingsStage = new Stage();
    settingsStage.initOwner(rootPane.getScene().getWindow());
    settingsStage.initModality(Modality.APPLICATION_MODAL); // Block main menu

    settingsStage.initStyle(StageStyle.TRANSPARENT);

    Scene scene = new Scene(settingsView);
    scene.setFill(Color.TRANSPARENT);

    settingsStage.setScene(scene);

    StageUtils.configureScreenSize(settingsStage);

    settingsStage.showAndWait();
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

    centerDropPane.setVisible(false);

    rootPane.getStylesheets().addAll(getClass().getResource(UI.BASE_CSS).toExternalForm(),
        getClass().getResource(UI.MAIN_MENU_CSS).toExternalForm());

    user = userData.findAll();

    boolean isLibraryEmpty = user.getLibrary().isEmpty();
    centerDropPane.setVisible(isLibraryEmpty);

    SideDrawerController.setMainMenuController(this);
    GameSelectController.setMainMenuController(this);
    GameSelectController.setUser(user);
    GameSelectController.setGameService(gameService);
    GameService.setMainMenuController(this);

    Platform.runLater(() -> {
      Stage stage = (Stage) rootPane.getScene().getWindow();
      if (stage != null) {
        windowHandler.initHandler(stage);
      }
    });

    loadGames();
  }

  private void loadGames() {
    if (user == null)
      return;

    CompletableFuture.runAsync(() -> {
      try {
        gameService.loadGames(user, centerDropPane, cardContainer);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public StackPane getRootPane() {
    return rootPane;
  }
}
