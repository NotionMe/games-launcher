package ua.notion.controllers;

import static java.lang.System.Logger.Level.INFO;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import javafx.scene.input.Dragboard;
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
import ua.notion.utils.ArchiveHelper;
import ua.notion.utils.StageUtils;
import ua.notion.utils.WindowHandler;
import javafx.fxml.FXMLLoader;

public class MainMenuController {

  private WindowHandler windowHandler;

  private User user;

  private static final System.Logger LOGGER = System.getLogger(MainMenuController.class.getName());

  private final UserRepository userData = new UserData();
  private final IconService iconService = new IconService();
  private static final WindowHelper WINDOW_HELPER = new WindowHelper();
  private final ArchiveHelper archiveHelper = new ArchiveHelper();
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
    Dragboard db = event.getDragboard();
    List<File> files = db.getFiles().stream()
        .filter(file -> ArchiveHelper.detectedArchive(file.getName()) != null).toList();

    boolean mockMultipleExecutables = true;

    CompletableFuture<List<File>> extractionFuture = CompletableFuture.supplyAsync(() -> {
      return files.stream().findFirst().map(file -> {
        try {
          String type = ArchiveHelper.detectedArchive(file.getName());
          List<File> result;

          if (type != null && (type.equals(".zip") || type.equals(".rar") || type.equals(".7z"))) {
            result = archiveHelper.extractSmartArchive(file.getAbsolutePath(), new File("cache/"));
          } else {
            result = archiveHelper.extractTarArchive(file.getAbsolutePath(), "cache/");
          }

          if (result == null)
            return null;
          return result.stream().filter(exe -> exe.getName().endsWith(".exe")).toList();
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }).orElse(null);
    });

    if (mockMultipleExecutables && !files.isEmpty()) {
      extractionFuture.thenAccept(extractedFiles -> {
        if (extractedFiles != null) {
          Platform.runLater(() -> showArchiveSelectionModal(extractedFiles));
        }
      });
      return;
    }

    // gameService.createCardsOnFiles(event, user, cardContainer, centerDropPane);
    // gameService.hidePanelVisible(dropFileInfo);
    // if (!UserData.fileIsExists()) {
    // gameService.showPanelVisible(centerDropPane);
    // }

    // if (!files.isEmpty() && files.get(0).exists()) {
    // gameService.hidePanelVisible(centerDropPane);
    // } else {
    // gameService.showPanelVisible(centerDropPane); // if 'files' not 'rar','zip','exe'
    // }
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

  private void showArchiveSelectionModal(List<File> archiveFile) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(Views.ARCHIVE_SELECTOR));
      Parent root = loader.load();

      ArchiveSelectorController controller = loader.getController();

      controller.setFiles(archiveFile);

      CompletableFuture<String> resultFuture = new CompletableFuture<>();
      controller.setResultFuture(resultFuture);

      Stage stage = new Stage();
      stage.initOwner(rootPane.getScene().getWindow());
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initStyle(StageStyle.TRANSPARENT);

      Scene scene = new Scene(root);
      scene.setFill(Color.TRANSPARENT);
      stage.setScene(scene);

      StageUtils.configureScreenSize(stage);
      stage.show();

      resultFuture.thenAccept(selectedFile -> {
        Platform.runLater(() -> {
          LOGGER.log(INFO, "Select file {0}", archiveFile);
          String gameName = handleArhiveSelection(selectedFile, '.');
          navigateToGameSelect(selectedFile, gameName);
        });
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String handleArhiveSelection(String targetFile, Character findChar) {
    File file = new File(targetFile);
    String fileName = file.getName();

    int lastIndex = fileName.lastIndexOf(findChar);
    return (lastIndex == -1) ? fileName : fileName.substring(0, lastIndex);
  }

  private void navigateToGameSelect(String selectedFile, String gameName) {
    Parent gameSelectRoot = (Parent) WINDOW_HELPER.navigateAdd(Views.GAME_SELECT);
    if (gameSelectRoot != null) {
      GameSelectController gameSelectController = WINDOW_HELPER.getLastLoader().getController();

      gameSelectView = gameSelectRoot;
      centerLayer.getChildren().add(gameSelectRoot);
      gameService.hidePanelVisible(bottomAnchorGroup);

      gameSelectController.setExecutablePathField(selectedFile);
      gameSelectController.setGameTitleField(gameName);
    }
  }

  public StackPane getRootPane() {
    return rootPane;
  }
}
