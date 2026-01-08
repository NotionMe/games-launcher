package ua.notion.controllers;

import static java.lang.System.Logger.Level.ERROR;

import java.io.File;
import java.lang.System.Logger;
import java.util.concurrent.CompletableFuture;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.services.GameLauncher;
import ua.notion.services.ImageService;
import ua.notion.ui.animation.AnimationHelper;
import ua.notion.ui.fx.WindowHelper;
import ua.notion.components.Game;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.Constants.Views;
import ua.notion.utils.OsUtils;

public class SideDrawerController {

  private static SideDrawerController instance;

  public SideDrawerController() {
  }

  private static final Logger LOGGER = System.getLogger(SideDrawerController.class.getName());

  @FXML
  private ImageView headerImageView;
  @FXML
  private Button playButton;
  @FXML
  private Label timeLabel;
  @FXML
  private GridPane actionsGrid;
  @FXML
  private Button debugButton;
  @FXML
  private Button settingsButton;
  @FXML
  private Button removeButton;
  @FXML
  private Button utilButton;
  @FXML
  private Button runButton;
  @FXML
  private Button foldersButton;
  @FXML
  private Button steamdbButton;
  @FXML
  private Button protonButton;
  @FXML
  private Button steamButton;
  @FXML
  private ToggleButton desktopButton;
  @FXML
  private ToggleButton appMenuButton;
  @FXML
  private ToggleButton nonSteamButton;

  private double xOffset = 0;
  private double yOffset = 0;

  private final UserRepository userData = new UserData();
  private final GameLauncher gameLauncher = new GameLauncher();

  private static MainMenuController mainMenuController;

  private static final AnimationHelper animationHelper = new AnimationHelper();
  private static final WindowHelper WINDOW_HELPER = new WindowHelper();

  ImageService imageService = new ImageService();

  private Game currentGame;

  @FXML
  private AnchorPane drawerRoot;
  @FXML
  private Pane drawerScrim;
  @FXML
  private Pane headerDragArea;
  @FXML
  private AnchorPane sideDrawer;

  @FXML
  private void initialize() {
    instance = this;
    drawerRoot.getStylesheets()
        .addAll(getClass().getResource(UI.SIDE_DRAWER_CSS).toExternalForm());
  }

  public static void setMainMenuController(MainMenuController mainMenuController) {
    SideDrawerController.mainMenuController = mainMenuController;
  }

  @FXML
  private void scrimPressAction() {
    closeDrawer();
  }

  @FXML
  protected void headerPressAction(MouseEvent event) {
    Stage stage = (Stage) drawerRoot.getScene().getWindow();

    if (!stage.isFullScreen()) {
      xOffset = stage.getX() - event.getScreenX();
      yOffset = stage.getY() - event.getScreenY();
    }
  }

  @FXML
  protected void headerMoveAction(MouseEvent event) {
    Stage stage = (Stage) drawerRoot.getScene().getWindow();

    if (!stage.isFullScreen()) {
      stage.setX(event.getScreenX() + xOffset);
      stage.setY(event.getScreenY() + yOffset);
    }
  }

  public static void show(Game game) {
    if (instance == null) {
      if (mainMenuController == null) {
        LOGGER.log(ERROR, "MainMenuController is not set! Cannot show drawer.");
        return;
      }
      Node node = WINDOW_HELPER.navigateAdd(Views.SIDE_DRAWER);
      if (node != null) {
        mainMenuController.getRootPane().getChildren().add(node);
      }
    }
    if (instance != null) {
      instance.openDrawer(game);
    } else {
      LOGGER.log(ERROR, "Failed to initialize SideDrawerController instance.");
    }
  }

  private void openDrawer(Game game) {
    currentGame = game;

    drawerRoot.setVisible(true);
    drawerRoot.setManaged(true);

    boolean isAnimationDisabled = userData.findAll().getLauncherSettings().isAnimationDisabled();

    if (game != null) {
      animationHelper.transitionToBackground(game.coverPath(),
          mainMenuController.getBackgroundImageView(), isAnimationDisabled);
      headerImageView.setImage(
          imageService.loadImage(game.coverPath(), UI.DEFAULT_COVER_PATH, getClass()));
    }

    if (isAnimationDisabled) {
      sideDrawer.setTranslateX(0);
    } else {

      TranslateTransition tt = new TranslateTransition(Duration.millis(150), sideDrawer);
      tt.setFromX(sideDrawer.getPrefWidth());
      tt.setToX(0);
      tt.play();
    }
  }

  public static void closeDrawer() {
    if (instance == null) {
      return;
    }

    boolean isAnimationDisabled = instance.userData.findAll().getLauncherSettings()
        .isAnimationDisabled();

    animationHelper.restoreDefaultBackground(mainMenuController.getBackgroundImageView(),
        isAnimationDisabled);

    if (isAnimationDisabled) {
      instance.sideDrawer.setTranslateX(instance.sideDrawer.getPrefWidth());
      instance.drawerRoot.setVisible(false);
      instance.drawerRoot.setManaged(false);
    } else {
      TranslateTransition tt =
          new TranslateTransition(Duration.millis(150), instance.sideDrawer);
      tt.setFromX(0);
      tt.setToX(instance.sideDrawer.getPrefWidth());
      tt.setOnFinished(event -> {
        instance.drawerRoot.setVisible(false);
        instance.drawerRoot.setManaged(false);
      });
      tt.play();
    }
  }

  @FXML
  private void onPlayAction() {
    String originalText = playButton.getText();
    playButton.setDisable(true);
    playButton.setText("Launch...");

    CompletableFuture.runAsync(() -> {
      try {
        User user = userData.findAll();
        gameLauncher.play(currentGame, user);
      } catch (Exception e) {
        LOGGER.log(ERROR, "Failed to launch game: " + currentGame.title(), e);
      }
    }).whenComplete((result, error) ->
        Platform.runLater(() -> {
          playButton.setDisable(false);
          playButton.setText(originalText);
        }));
  }

  @FXML
  private void onDebugAction() {
    System.out.println("you press button Debug");
  }

  @FXML
  private void onSettingsAction() {
    System.out.println("you press button Settings");
  }

  @FXML
  private void onRemoveAction() {
    System.out.println("you press button Remove");
  }

  @FXML
  private void onUtilAction() {
    System.out.println("you press button Utility");
  }

  @FXML
  private void onRunAction() {
    System.out.println("you press button Run");
  }

  @FXML
  private void onFoldersAction() {

    if (currentGame == null || currentGame.targetPath() == null) {
      return;
    }

    File parentDir = new File(currentGame.targetPath()).getParentFile();
    String pfxPath = currentGame.pfx();

    if (OsUtils.isLinux()) {
      ContextMenu contextMenu = new ContextMenu();

      MenuItem game = new MenuItem("Game");
      MenuItem prefix = new MenuItem("Prefix");

      game.setOnAction(event -> OsUtils.openPath(parentDir));
      prefix.setOnAction(event -> {
        if (pfxPath != null && !pfxPath.isBlank()) {
          OsUtils.openPath(new File(pfxPath));
          contextMenu.hide();
        } else {
          LOGGER.log(ERROR, "Error! Path to prefix not set!");
        }
      });

      contextMenu.getItems().addAll(game, prefix);
      contextMenu.show(foldersButton, Side.BOTTOM, 0, 0);
    }
    else{
      OsUtils.openPath(parentDir);
    }
  }

  @FXML
  private void onSteamDbAction() {
    System.out.println("you press button SteamDB");
  }

  @FXML
  private void onProtonAction() {
    System.out.println("you press button Proton");
  }

  @FXML
  private void onSteamAction() {
    System.out.println("you press button Steam");
  }

  @FXML
  private void onDesktopAction() {
    System.out.println("you press button Desktop shortcut");
  }

  @FXML
  private void onAppMenuAction() {
    System.out.println("you press button App menu shortcut");
  }

  @FXML
  private void onNonSteamAction() {
    System.out.println("you press button Non-steam shortcut");
  }

  public AnchorPane getDrawerRoot() {
    return drawerRoot;
  }
}