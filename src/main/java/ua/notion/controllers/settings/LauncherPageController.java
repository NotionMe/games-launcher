package ua.notion.controllers.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.ToggleSwitch;

public class LauncherPageController implements Initializable {

  private static final String BASE_CSS = "/css/base.css";
  private static final String LAUNCHER_PAGE_CSS = "/css/settings/launcher-page.css";

  @FXML
  private StackPane rootPane;

  @FXML
  private ToggleSwitch fullWindowSwitch;

  @FXML
  private ToggleSwitch noSteamSwitch;

  @FXML
  private ToggleSwitch disableAnimSwitch;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    rootPane.getStylesheets().addAll(
        getClass().getResource(LAUNCHER_PAGE_CSS).toExternalForm(),
        getClass().getResource(BASE_CSS).toExternalForm()
    );
  }
}