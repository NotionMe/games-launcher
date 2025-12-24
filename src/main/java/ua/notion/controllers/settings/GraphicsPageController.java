package ua.notion.controllers.settings;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.ToggleSwitch;
import ua.notion.utils.Constants.UI;

public class GraphicsPageController {


  @FXML
  private StackPane rootPane;

  @FXML
  private ToggleSwitch wineSwitch;
  @FXML
  private ToggleSwitch useNativeWayland;

  public void initialize() {
    rootPane.getStylesheets().addAll(
        getClass().getResource(UI.GRAPHICS_PAGE_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm()
    );
  }
}
