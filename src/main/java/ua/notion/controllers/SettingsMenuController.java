package ua.notion.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import ua.notion.utils.WindowHandler;

public class SettingsMenuController implements Serializable {

  private static final String BASE_CSS = "/css/base.css";
  private static final String SETTINGS_MENU_CSS = "/css/settings-menu.css";

  @FXML
  private StackPane contentArea;

  @FXML
  private Button closeButton;
  @FXML
  private Button minButton;
  @FXML
  private Button fullButton;
  @FXML
  private Button pathsButton;
  @FXML
  private Button protonsButton;
  @FXML
  private Button graphicsButton;
  @FXML
  private Button launcherButton;
  @FXML
  private Button aboutButton;

  @FXML
  private BorderPane rootPane;
  @FXML
  private AnchorPane topPane;

  private WindowHandler windowHandler;

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

  @FXML
  private void initialize() {
    this.windowHandler = new WindowHandler(topPane);

      rootPane.getStylesheets().addAll(
          getClass().getResource(SETTINGS_MENU_CSS).toExternalForm(),
          getClass().getResource(BASE_CSS).toExternalForm()
      );

      loadPage("paths-page.fxml");
  }

  private void loadPage(String fxmlFileName) {
    try {
      URL fxmlUrl = getClass().getResource("/ua/notion/settings/" + fxmlFileName);
      if (fxmlUrl == null) {
        System.out.println("File not found: " + fxmlFileName);
        return;
      }

      Parent view = FXMLLoader.load(fxmlUrl);

      contentArea.getChildren().removeAll();
      contentArea.getChildren().setAll(view);

    } catch (IOException e) {
      Logger.getLogger(SettingsMenuController.class.getName()).log(Level.SEVERE, null, e);
    }
  }
}