package ua.notion.controllers.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.ToggleSwitch;

public class GraphicsPageController implements Initializable {

  private static final String BASE_CSS = "/css/base.css";
  private static final String GRAPHICS_PAGE_CSS = "/css/settings/graphics-page.css";

  @FXML
  private ToggleSwitch wineSwitch;
  @FXML
  private ToggleSwitch useNativeWayland;

  @FXML
  private StackPane rootPane;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    rootPane.getStylesheets().addAll(
        getClass().getResource(GRAPHICS_PAGE_CSS).toExternalForm(),
        getClass().getResource(BASE_CSS).toExternalForm()
    );
  }
}
