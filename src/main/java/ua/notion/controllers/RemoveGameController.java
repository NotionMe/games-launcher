package ua.notion.controllers;

import javafx.fxml.FXML;

import javafx.scene.layout.StackPane;
import ua.notion.utils.Constants.UI;

public class RemoveGameController {

  @FXML
  private StackPane rootPane;

  @FXML
  private void initialize() {
    rootPane.getStylesheets()
        .addAll(getClass().getResource(UI.REMOVE_GAME_POPUP_CSS).toExternalForm());
  }
}