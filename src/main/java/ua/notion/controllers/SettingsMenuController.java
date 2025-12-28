package ua.notion.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ua.notion.controllers.settings.LauncherPageController;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.Constants.Views;
import ua.notion.utils.WindowHandler;

public class SettingsMenuController {

  @FXML
  private StackPane contentArea;

  @FXML
  private Button closeButton;
  @FXML
  private Button minButton;
  @FXML
  private Button fullButton;

  @FXML
  private ToggleGroup navGroup;

  @FXML
  private ToggleButton pathsButton;
  @FXML
  private ToggleButton protonsButton;
  @FXML
  private ToggleButton graphicsButton;
  @FXML
  private ToggleButton launcherButton;
  @FXML
  private ToggleButton aboutButton;

  @FXML
  private BorderPane rootPane;
  @FXML
  private AnchorPane topPane;

  private WindowHandler windowHandler;

  public Stage getMainWindowStage(){
    Stage settingsStage = (Stage) rootPane.getScene().getWindow();

    return (Stage)settingsStage.getOwner();
  }

  @FXML
  private void onPathsButtonPressed(ActionEvent event) {
    loadPage("paths-page.fxml");
  }

  @FXML
  private void onProtonsButtonPressed(ActionEvent event) {
    loadPage("protons-page.fxml");
  }

  @FXML
  private void onGraphicsButtonPressed(ActionEvent event) {
    loadPage("graphics-page.fxml");
  }

  @FXML
  private void onLauncherButtonPressed(ActionEvent event) {
    loadPage("launcher-page.fxml");
  }

  @FXML
  private void onAboutButtonPressed(ActionEvent event) {
    loadPage("about-page.fxml");
  }

  @FXML
  private void initialize() {
    this.windowHandler = new WindowHandler(topPane);

    rootPane.getStylesheets().addAll(
        getClass().getResource(UI.SETTINGS_MENU_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm()
    );

    configureNavGroup();
    loadPage("paths-page.fxml");
  }

  @FXML
  protected void handleCloseAction(ActionEvent event) {
    windowHandler.close((Node) event.getSource());
  }

  @FXML
  protected void handleMinAction(ActionEvent event) {
    windowHandler.minimize((Node) event.getSource());
  }

  @FXML
  protected void handleFullAction(ActionEvent event) {
    windowHandler.toggleFullscreen((Node) event.getSource());
  }

  @FXML
  protected void handlePressAction(MouseEvent event) {
    windowHandler.onPress(event);
  }

  @FXML
  protected void handleMovementAction(MouseEvent event) {
    windowHandler.onDrag(event);
  }

  // Ensures that at least one button in the navigation group remains selected
  private void configureNavGroup() {
    navGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null && oldVal != null) {
        navGroup.selectToggle(oldVal);
      }
    });
  }

  private void loadPage(String fxmlFileName) {
    try {
      URL fxmlUrl = getClass().getResource(Views.SETTINGS_BASE_DIR + fxmlFileName);
      if (fxmlUrl == null) {
        System.out.println("File not found: " + fxmlFileName);
        return;
      }

      FXMLLoader loader = new FXMLLoader(fxmlUrl);
      Parent view = loader.load();

      Object controller = loader.getController();
      if (controller instanceof LauncherPageController) {
        ((LauncherPageController) controller).setParentController(this);
      }

      // KOSTIL!, i idk how do it another way
      if (view instanceof javafx.scene.layout.Region) {
        javafx.scene.layout.Region region = (javafx.scene.layout.Region) view;

        region.setMaxWidth(Double.MAX_VALUE);

        region.prefHeightProperty().bind(contentArea.heightProperty().multiply(0.7));
      }

      StackPane.setAlignment(view, javafx.geometry.Pos.TOP_CENTER);
      StackPane.setMargin(view, new javafx.geometry.Insets(50, 0, 0, 0));

      contentArea.getChildren().removeAll();
      contentArea.getChildren().setAll(view);

    } catch (IOException e) {
      Logger.getLogger(SettingsMenuController.class.getName()).log(Level.SEVERE, null, e);
    }
  }
}