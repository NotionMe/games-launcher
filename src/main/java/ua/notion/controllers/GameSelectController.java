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
import ua.notion.utils.Constants.Data;
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

public class GameSelectController {
    private static final Logger LOGGER = System.getLogger(GameSelectController.class.getName());
    private static MainMenuController mainMenuController;
    private static GameService gameService;

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
    private TextField executablePathField;
    @FXML
    private Button runInstallerButton;
    @FXML
    private Button finishButton;
    @FXML
    private ImageView imagePreview;

    @FXML
    public void initialize() {
        platformComboBox.getItems().setAll("Windows", "Linux");
        platformComboBox.getSelectionModel().select("Windows");

        previewLableText.textProperty().bind(gameTitleField.textProperty());

        imagePathField.textProperty().addListener((observable, defaultImage, newImage) -> {
            URI url = ImageService.checkUriImage(newImage);
            if (url != null) {
                try {
                    imagePreview.setImage(new Image(url.toString(), true));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //TODO: треба зробити КАЧЕСТВЕНОоооо.
        addProtonCheckBox();
    }

    public static void setMainMenuController(MainMenuController mainMenuController) {
        GameSelectController.mainMenuController = mainMenuController;
    }

    public static void setGameService(GameService gameService) {
        GameSelectController.gameService = gameService;
    }

    @FXML
    public void onBackButton(ActionEvent event) {
        LOGGER.log(Level.INFO, "BACK BUTTON PRESSED");
        System.out.println(mainMenuController.getGameSelectView());
        try {
            if (mainMenuController.getGameSelectView() != null) {
                gameService.showPanelVisible(mainMenuController.getBottomAnchorGroup());
                mainMenuController.setGameSelectView(null);
                mainMenuController.getCenterLayer().getChildren()
                        .remove(mainMenuController.getCenterLayer().getChildren().size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBrowseImage(ActionEvent event) {
        LOGGER.log(Level.INFO, "BROWSE IMAGE");
        File file = gameService.getFilePath(Data.SUPPORTED_EXTENSIONS_IMAGE, "Choice image");
        if (file.getPath() != null)
            imagePathField.setText(file.getPath());
    }

    @FXML
    public void onDefaultWineSettingsAction(ActionEvent event) {
        LOGGER.log(Level.INFO, "ENABLE DEFAULT WINE");
    }

    @FXML
    public void onBrowseWinePrefix(ActionEvent event) {
        LOGGER.log(Level.INFO, "WINE PREFIX");
        Stage stage = (Stage) mainMenuController.getRootPane().getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choice folder");
        File fileSelect = directoryChooser.showDialog(stage);
        if (fileSelect.getPath() != null)
            winePrefixField.setText(fileSelect.getPath());

    }

    @FXML
    public void onBrowseExecutable(ActionEvent event) {
        LOGGER.log(Level.INFO, "PATH TO EXE");
    }

    @FXML
    public void onRunInstaller(ActionEvent event) {
        LOGGER.log(Level.INFO, "RUN INSTALLER");

    }

    @FXML
    public void onFinish(ActionEvent event) {
        LOGGER.log(Level.INFO, "FINISH");
    }

    // Поки що халтурщіна ну і похер )))
    private void addProtonCheckBox() {
        File[] files = GameService.getProtonVersionHost(Data.PROTON_PATH.toString());
        if (files.length > 0) {
            for (File file : files) {
                wineVersionComboBox.getItems().add(file.getName());
            }
            wineVersionComboBox.getSelectionModel().selectFirst();
        }
    }
}
