package ua.notion.controllers.settings;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import ua.notion.components.User;
import ua.notion.controllers.SettingsMenuController;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.utils.Constants.UI;

public class LauncherPageController {

  private User user;

  private SettingsMenuController settingsMenuController;

  private final UserRepository userData = new UserData();

  private static final System.Logger LOGGER = System.getLogger(LauncherPageController.class.getName());

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

    user = userData.findAll();

    initSwitchesState();

    setNoSteamSwitch();
    setDisableAnimSwitch();
    setFullWindowSwitchListener();
  }

  public void setParentController(SettingsMenuController controller) {
    this.settingsMenuController = controller;

    Stage stage = settingsMenuController.getMainWindowStage();

    if (stage != null) {
      stage.fullScreenProperty().addListener((obs, wasFull, isNowFull) -> {
        if (fullWindowSwitch.isSelected() != isNowFull) {
          fullWindowSwitch.setSelected(isNowFull);
        }
      });
    }
    applyFullScreenSetting();
  }

  private void initSwitchesState() {
    if (user.getLauncherSettings() != null) {
      fullWindowSwitch.setSelected(user.getLauncherSettings().isFullScreen());
      noSteamSwitch.setSelected(user.getLauncherSettings().isSteamDisabled());
      disableAnimSwitch.setSelected(user.getLauncherSettings().isAnimationDisabled());
    }
  }

  private void setFullWindowSwitchListener() {
    fullWindowSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
      LOGGER.log(INFO, "User changed FullScreen to: " + newValue);

      user.getLauncherSettings().setFullScreen(newValue);
      userData.save(user);

      applyFullScreenSetting();
    });
  }

  private void applyFullScreenSetting() {
    if (settingsMenuController != null && settingsMenuController.getMainWindowStage() != null) {
      boolean shouldBeFullScreen = user.getLauncherSettings().isFullScreen();
      boolean current = settingsMenuController.getMainWindowStage().isFullScreen();

      if (current != shouldBeFullScreen) {
        LOGGER.log(DEBUG, "Applying FullScreen state: " + shouldBeFullScreen);
        settingsMenuController.getMainWindowStage().setFullScreen(shouldBeFullScreen);
      }
    }
  }

  private void setNoSteamSwitch() {
    noSteamSwitch.selectedProperty().addListener((observable, oldValue,
        newValue) -> {
      LOGGER.log(INFO, "User changed Steam Disabled to: " + newValue);

      user.getLauncherSettings().setSteamDisabled(newValue);
      userData.save(user);
    });
  }

  private void setDisableAnimSwitch() {
    disableAnimSwitch.selectedProperty().addListener((observable, oldValue,
        newValue) -> {
      LOGGER.log(INFO, "User changed Animation Disabled to: " + newValue);

      user.getLauncherSettings().setAnimationDisabled(newValue);
      userData.save(user);
    });
  }
}