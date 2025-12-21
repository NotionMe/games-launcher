package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import ua.notion.utils.Constants;
import ua.notion.utils.Constants.UI;

public class SettingsMenuController {

  @FXML
  private AnchorPane rootPane;

  @FXML
  private void initialize() {
    rootPane.getStylesheets()
        .addAll(getClass().getResource(UI.SETTINGS_MENU_CSS).toExternalForm());
  }
}
