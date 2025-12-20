package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class SettingsMenuController {

    private static final String SETTINGS_MENU_CSS = "/css/settings-menu.css";

    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize() {
        rootPane.getStylesheets()
                .addAll(getClass().getResource(SETTINGS_MENU_CSS).toExternalForm());
    }


}
