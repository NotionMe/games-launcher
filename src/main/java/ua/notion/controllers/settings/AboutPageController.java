package ua.notion.controllers.settings;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import ua.notion.utils.Constants.UI;

public class AboutPageController {

  @FXML
  private StackPane rootPane;

  public void initialize() {
    rootPane.getStylesheets().addAll(
        getClass().getResource(UI.ABOUT_PAGE_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm()
    );
  }
}
