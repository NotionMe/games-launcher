package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ua.notion.services.GameService;
import ua.notion.services.ImageService;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.utils.SteamGridDB;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.Constants.UI;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.File;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Arrays;
import java.util.List;
import ua.notion.ui.fx.WindowHelper;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;
import ua.notion.utils.Config;
import ua.notion.utils.OsUtils;

public class GameSelectController {

  private final WindowHelper windowHelper = new WindowHelper();
  private final SteamGridDB steamGridDB = new SteamGridDB(Config.getSteamGridDBApiKey());
  private final UserRepository userRepository = new UserData();
  private static final Logger LOGGER = System.getLogger(GameSelectController.class.getName());
  private static MainMenuController mainMenuController;
  private static GameService gameService;
  private final PauseTransition debounce = new PauseTransition(Duration.millis(800));
  private final ImageService imageService = new ImageService();

  @FXML
  private AnchorPane rootPane;
  @FXML
  private Label previewLabelText;
  @FXML
  private TextField gameTitleField;
  @FXML
  private TextField imagePathField;
  @FXML
  private ComboBox<String> platformComboBox;
  @FXML
  private TitledPane wineSettingsPane;
  @FXML
  private CheckBox defaultWineSettingsCheckBox;
  @FXML
  private TextField winePrefixField;
  @FXML
  private ComboBox<String> wineVersionComboBox;
  @FXML
  private CheckBox argumentsCheckBox;
  @FXML
  private VBox argumentsContainer;
  @FXML
  private TextField wineDllOverridesField;
  @FXML
  private TextField executablePathField;

  @FXML
  private TextField argumentsField;
  @FXML
  private Button runInstallerButton;
  @FXML
  private Button finishButton;
  @FXML
  private ImageView imagePreview;
  @FXML
  private ImageView previewGameIcon;
  @FXML
  private TextField iconPathField;
  @FXML
  private CheckBox wineDllOverridesCheckBox;
  @FXML
  private VBox platformView;

  @FXML
  private void initialize() {
    argumentsContainer.visibleProperty().bind(argumentsCheckBox.selectedProperty());
    argumentsContainer.managedProperty().bind(argumentsCheckBox.selectedProperty());

    argumentsCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
      if (!isSelected) {
        argumentsField.clear();
      }
    });

    platformComboBox.getItems().setAll("Windows", "Linux");
    platformComboBox.getSelectionModel().select("Linux");
    // default dll overide for online fix
    wineDllOverridesField
        .setText("OnlineFix64=n;SteamOverlay64=n;winmm=n,b;dnet=n;steam_api64=n;winhttp=n,b");

    previewLabelText.textProperty().bind(gameTitleField.textProperty());

    imagePathField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null || newValue.isBlank()) {
        setDefaultPreview(UI.DEFAULT_COVER_PATH, imagePreview);
        return;
      }

      imagePreview.setImage(imageService.loadImage(newValue, UI.DEFAULT_COVER_PATH, getClass()));
    });

    iconPathField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null || newValue.isBlank()) {
        setDefaultPreview(UI.DEFAULT_ICON_PATH, previewGameIcon);
        return;
      }

      previewGameIcon.setImage(imageService.loadImage(newValue, UI.DEFAULT_ICON_PATH, getClass()));

    });

    addProtonCheckBox();
    setDefaultPreview(UI.DEFAULT_COVER_PATH, imagePreview);
    setDefaultPreview(UI.DEFAULT_ICON_PATH, previewGameIcon);

    wineDllOverridesField.disableProperty().bind(wineDllOverridesCheckBox.selectedProperty().not());

    setupDebouncedSearch();
    platformComboBox.valueProperty().addListener((obs, oldVal, newVal) -> setupPlatform());
    setupPlatform();
  }

  private void setupPlatform() {
    String selectedPlatform = platformComboBox.getValue();
    if (selectedPlatform == null) {
      return;
    }
    boolean isWindowsHost = OsUtils.isWindows();
    boolean isLinuxTarget = selectedPlatform.equalsIgnoreCase("Linux");

    boolean showWine = !isWindowsHost && !isLinuxTarget;

    wineSettingsPane.setVisible(showWine);
    wineSettingsPane.setManaged(showWine);

    platformView.setVisible(!isWindowsHost);
    platformView.setManaged(!isWindowsHost);
  }

  private void setupDebouncedSearch() {
    debounce.setOnFinished(e -> getImageFromSteamDB());
    gameTitleField.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue == null || newValue.isBlank()) {
        return;
      }
      debounce.playFromStart();
    });
  }

  public void setGameTitleField(String gameTitleField) {
    this.gameTitleField.setText(gameTitleField);
  }

  public void setExecutablePathField(String executablePathField) {
    this.executablePathField.setText(executablePathField);
  }

  private void setDefaultPreview(String defaultPath, ImageView imageView) {
    imageView.setImage(imageService.loadImage(null, defaultPath, getClass()));
  }

  public static void setMainMenuController(MainMenuController mainMenuController) {
    GameSelectController.mainMenuController = mainMenuController;
  }

  public static void setGameService(GameService gameService) {
    GameSelectController.gameService = gameService;
  }

  @FXML
  private void onBackButton(ActionEvent event) {
    LOGGER.log(Level.INFO, "BACK BUTTON PRESSED");
    if (mainMenuController.getGameSelectView() != null) {
      mainMenuController.setGameSelectView(null);
      windowHelper.navigateBack(mainMenuController.getCenterLayer(),
          mainMenuController.getBottomAnchorGroup());
    }
  }

  @FXML
  private void onBrowseImage(ActionEvent event) {
    browsePath(imagePathField, Data.SUPPORTED_EXTENSIONS_IMAGE, "Choice image", false);
  }

  @FXML
  private void onBrowseIcon(ActionEvent event) {
    browsePath(iconPathField, Data.SUPPORTED_EXTENSIONS_IMAGE, "Choice icon", false);
  }

  @FXML
  private void onDefaultWineSettingsAction(ActionEvent event) {
    LOGGER.log(Level.INFO, "ENABLE DEFAULT WINE");
  }

  @FXML
  private void onBrowseWinePrefix(ActionEvent event) {
    browsePath(winePrefixField, null, "Choice folder", true);
  }

  @FXML
  private void onBrowseExecutable(ActionEvent event) {
    browsePath(executablePathField, Data.SUPPORTED_EXTENSIONS_GAME, "Choice exe file", false);
  }

  @FXML
  private void onRunInstaller(ActionEvent event) {
    LOGGER.log(Level.INFO, "RUN INSTALLER");
    // GameLauncher.launch(game);
  }

  @FXML
  private void onFinish(ActionEvent event) {
    setupGames();
  }

  private boolean checkFields() {
    return !gameTitleField.getText().isBlank() && !executablePathField.getText().isBlank();
  }

  private void addProtonCheckBox() {
    File[] files = GameService.getProtonVersionHost(Data.PROTON_PATH.toString());

    if (files != null && files.length > 0) {
      List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
      wineVersionComboBox.getItems().addAll(fileNames);
    }
    wineVersionComboBox.getSelectionModel().selectFirst();
  }

  private void browsePath(TextField targetField, List<String> extensions, String description,
      boolean isDirectory) {
    File file;
    if (isDirectory) {
      Stage stage = (Stage) targetField.getScene().getWindow();
      file = directoryChooser(description, stage);
    } else {
      file = gameService.getFilePath(extensions, description);
    }

    if (file != null && file.getPath() != null && !file.getPath().isEmpty()) {
      targetField.setText(file.getPath());
    }
  }

  private void setupGames() {
    User user = userRepository.findAll();

    Image iconPath = imageService.loadImage(iconPathField.getText(), UI.DEFAULT_COVER_PATH, getClass());
    Image coverPath = imageService.loadImage(imagePathField.getText(), UI.DEFAULT_COVER_PATH, getClass());

    String protonPath = new File(Data.PROTON_PATH, wineVersionComboBox.getValue()).getAbsolutePath();

    String wineDllOverrides = wineDllOverridesCheckBox.isSelected() ? wineDllOverridesField.getText() : "";

    String launchArguments = (argumentsField != null) ? argumentsField.getText() : "";

    Game game = new Game(gameTitleField.getText(), executablePathField.getText(), launchArguments,
        winePrefixField.getText(), wineDllOverrides, protonPath, iconPath.getUrl(),
        coverPath.getUrl());
    if (game != null && checkFields()) {
      user.addGame(game);
      userRepository.save(user);
      if (mainMenuController.getGameSelectView() != null) {
        mainMenuController.setGameSelectView(null);
        windowHelper.navigateBack(mainMenuController.getCenterLayer(),
            mainMenuController.getBottomAnchorGroup());
      }
      try {
        gameService.createGameCard(game, mainMenuController.getCardContainer());
        gameService.hidePanelVisible(mainMenuController.getCenterDropPane());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private File directoryChooser(String nametitle, Stage stage) {
    DirectoryChooser dc = new DirectoryChooser();
    dc.setTitle(nametitle);
    return dc.showDialog(stage);
  }

  private void getImageFromSteamDB() {
    String title = gameTitleField.getText();
    if (title == null || title.isBlank()) {
      return;
    }

    CompletableFuture.runAsync(() -> {
      try {
        int gameId = steamGridDB.getGameIdByName(title);
        if (gameId != -1) {
          var gridsResponse = steamGridDB.getGridsByGameId(gameId);
          // String coverUrl = steamGridDB.getFirstImageUrl(gridsResponse);
          String coverUrl = steamGridDB.getRandomImageUrl(gridsResponse);

          var iconsResponse = steamGridDB.getIconsByGameId(gameId);
          // String iconUrl = steamGridDB.getFirstImageUrl(iconsResponse);
          String iconUrl = steamGridDB.getRandomImageUrl(iconsResponse);

          if (coverUrl != null) {
            imageService.checkUriImage(coverUrl);
          }
          if (iconUrl != null) {
            imageService.checkUriImage(iconUrl);
          }

          Platform.runLater(() -> {
            if (coverUrl != null) {
              imagePathField.setText(coverUrl);
            }
            if (iconUrl != null) {
              iconPathField.setText(iconUrl);
            }
          });
        }
      } catch (Exception e) {
        LOGGER.log(Level.WARNING, "Failed to fetch images from SteamGridDB", e);
      }
    });
  }

}
