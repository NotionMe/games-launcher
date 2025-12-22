package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import ua.notion.utils.Constants.Views;
import javafx.scene.control.CheckBox;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;

public class GameSelectController {
    private static final Logger LOGGER = System.getLogger(GameSelectController.class.getName());
    private static MainMenuController mainMenuController;

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
    public void initialize() {
        platformComboBox.getItems().setAll("Windows", "Linux");
        platformComboBox.getSelectionModel().select("Windows");
    }

    public static void setMainMenuController(MainMenuController mainMenuController) {
        GameSelectController.mainMenuController = mainMenuController;
    }

    @FXML
    public void onBackButton(ActionEvent event) {
        LOGGER.log(Level.INFO, "BACK BUTTON PRESSED");
        try {
            if (!mainMenuController.getCenterLayer().getChildren().isEmpty()) {
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
    }

    @FXML
    public void onDefaultWineSettingsAction(ActionEvent event) {
        LOGGER.log(Level.INFO, "ENABLE DEFAULT WINE");
    }

    @FXML
    public void onBrowseWinePrefix(ActionEvent event) {
        LOGGER.log(Level.INFO, "WINE PREFIX");
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

    public void setWineVersions(List<String> versions) {
        wineVersionComboBox.getItems().setAll(versions);
        if (!versions.isEmpty()) {
            wineVersionComboBox.getSelectionModel().selectFirst();
        }
    }
}
