package ua.notion.controllers.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

public class ProtonsPageController implements Initializable {

  private static final String BASE_CSS = "/css/base.css";
  private static final String PROTONS_PAGE_CSS = "/css/settings/protons-page.css";

  @FXML
  private StackPane rootPane;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    rootPane.getStylesheets().addAll(
        getClass().getResource(PROTONS_PAGE_CSS).toExternalForm(),
        getClass().getResource(BASE_CSS).toExternalForm()
    );
  }
}
