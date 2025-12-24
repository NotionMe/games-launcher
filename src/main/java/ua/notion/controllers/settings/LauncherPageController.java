package ua.notion.controllers.settings;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.ToggleSwitch;
import ua.notion.utils.Constants.UI;

public class LauncherPageController {



  @FXML
  private StackPane rootPane;

  @FXML
  private ToggleSwitch fullWindowSwitch;

  @FXML
  private ToggleSwitch noSteamSwitch;

  @FXML
  private ToggleSwitch disableAnimSwitch;

  public void initialize() {
    rootPane.getStylesheets().addAll(
        getClass().getResource(UI.LAUNCHER_PAGE_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm()
    );
  }
}