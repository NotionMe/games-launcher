package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ua.notion.services.GameService;
import ua.notion.services.ImageService;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.services.GameLauncher;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.Constants.Views;
import javafx.scene.control.CheckBox;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import java.io.File;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import ua.notion.ui.fx.WindowHelper;

public class GameSelectController {


    private final UserRepository userRepository = new UserData();
    private static User user;
    private static final Logger LOGGER = System.getLogger(GameSelectController.class.getName());
    private static MainMenuController mainMenuController;
    private static GameService gameService;
    private static String protonPath = null;

    @FXML
    private Label previewLableText;
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
    private TextField wineDllOverridesField;
    @FXML
    private TextField executablePathField;
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
    private void initialize() {
        platformComboBox.getItems().setAll("Windows", "Linux");
        platformComboBox.getSelectionModel().select("Windows");
        wineDllOverridesField.setText(
                "OnlineFix64=n;SteamOverlay64=n;winmm=n,b;dnet=n;steam_api64=n;winhttp=n,b");

        previewLableText.textProperty().bind(gameTitleField.textProperty());

        imagePathField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                setDefaultPreview(UI.DEFAULT_COVER_PATH, imagePreview);
                return;
            }

            URI url = ImageService.checkUriImage(newValue);
            if (url != null) {
                try {
                    imagePreview.setImage(new Image(url.toString(), true));
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to load image: {0}", url);
                    setDefaultPreview(UI.DEFAULT_COVER_PATH, imagePreview);
                }
            } else {
                setDefaultPreview(UI.DEFAULT_COVER_PATH, imagePreview);
            }
        });

        iconPathField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                setDefaultPreview(UI.DEFAULT_ICON_PATH, previewGameIcon);
                return;
            }

            URI url = ImageService.checkUriImage(newValue);
            if (url != null) {
                try {
                    previewGameIcon.setImage(new Image(url.toString(), true));
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to load icon: {0}", url);
                    setDefaultPreview(UI.DEFAULT_ICON_PATH, previewGameIcon);
                }
            } else {
                setDefaultPreview(UI.DEFAULT_ICON_PATH, previewGameIcon);
            }
        });

        // TODO: треба зробити КАЧЕСТВЕНОоооо.
        addProtonCheckBox();
        setDefaultPreview(UI.DEFAULT_COVER_PATH, imagePreview);
        setDefaultPreview(UI.DEFAULT_ICON_PATH, previewGameIcon);
    }

    private void setDefaultPreview(String defaultCoverPath, ImageView imageView) {
        URL resource = getClass().getResource(defaultCoverPath);
        if (resource != null) {
            Image defaultImage = new Image(resource.toExternalForm());
            imageView.setImage(defaultImage);
        }
    }

    public static void setMainMenuController(MainMenuController mainMenuController) {
        GameSelectController.mainMenuController = mainMenuController;
    }

    public static void setUser(User user) {
        GameSelectController.user = user;
    }

    public static void setGameService(GameService gameService) {
        GameSelectController.gameService = gameService;
    }

    @FXML
    private void onBackButton(ActionEvent event) {
        LOGGER.log(Level.INFO, "BACK BUTTON PRESSED");
        if (mainMenuController.getGameSelectView() != null) {
            mainMenuController.setGameSelectView(null);
            WindowHelper.navigateBack(mainMenuController.getCenterLayer(),
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
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.launch(null);
    }

    @FXML
    private void onFinish(ActionEvent event) {
        Game game = new Game(gameTitleField.getText(), executablePathField.getText(),
                winePrefixField.getText(), wineDllOverridesField.getText(),
                imagePathField.getText(), iconPathField.getText());
        user.addGame(game);
        userRepository.save(user);
        if (mainMenuController.getGameSelectView() != null) {
            mainMenuController.setGameSelectView(null);
            WindowHelper.navigateBack(mainMenuController.getCenterLayer(),
                    mainMenuController.getBottomAnchorGroup());
        }
    }

    // Поки що халтурщіна ну і похер )))
    private void addProtonCheckBox() {
        File[] files = GameService.getProtonVersionHost(Data.PROTON_PATH.toString());

        if (files.length > 0) {
            for (File file : files) {
                wineVersionComboBox.getItems().add(file.getName());
                // Поки що хардкод, треба переробити нормально!!! (Я займусь)
                protonPath = file.getPath();
            }
            wineVersionComboBox.getSelectionModel().selectFirst();
        }
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

    private File directoryChooser(String nametitle, Stage stage) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle(nametitle);
        return dc.showDialog(stage);
    }
}
