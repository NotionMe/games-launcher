package ua.notion.controllers.settings;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import ua.notion.controllers.MainMenuController;
import ua.notion.controllers.SettingsMenuController;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.WindowHandler;

public class LauncherPageController {

  private WindowHandler windowHandler;

  private MainMenuController mainMenuController;

  private SettingsMenuController settingsMenuController;

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

    setNoSteamSwitch();
    setDisableAnimSwitch();
  }

  public void setParentController(SettingsMenuController controller) {
    this.settingsMenuController = controller;

    if (this.settingsMenuController != null && this.settingsMenuController.getMainWindowStage() != null) {
      fullWindowSwitch.setSelected(settingsMenuController.getMainWindowStage().isFullScreen());

      setFullWindowSwitchListener();
    }
  }

  private void setFullWindowSwitchListener() {
    fullWindowSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
      if (settingsMenuController != null && settingsMenuController.getMainWindowStage() != null) {
        settingsMenuController.getMainWindowStage().setFullScreen(newValue);
      }
    });
  }

  private void setNoSteamSwitch() {
    noSteamSwitch.selectedProperty().addListener((observable, oldValue,
        newValue) -> {
      if (newValue) {
        System.out.println("Pressed");
      } else {
        System.out.println("FALSE");
      }
    });
  }

  private void setDisableAnimSwitch() {
    disableAnimSwitch.selectedProperty().addListener((observable, oldValue,
        newValue) -> {
      if (newValue) {
        System.out.println("Pressed");
      } else {
        System.out.println("FALSE");
      }
    });
  }
}