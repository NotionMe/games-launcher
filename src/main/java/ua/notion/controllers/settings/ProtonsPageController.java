package ua.notion.controllers.settings;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;
import ua.notion.utils.Constants.UI;

public class ProtonsPageController {

  @FXML
  private StackPane rootPane;

  @FXML
  private ChoiceBox<String> protonsChoiceBox;

  public void initialize() {
    rootPane.getStylesheets().addAll(
        getClass().getResource(UI.PROTONS_PAGE_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm()
    );

    protonsChoiceBox.getItems().add("GE-Proton Latest");
    protonsChoiceBox.getItems().add("GE-Proton Latest2");
    protonsChoiceBox.getItems().add("GE-Proton Latest3");

    protonsChoiceBox.setValue("GE-Proton Latest");
  }
}
