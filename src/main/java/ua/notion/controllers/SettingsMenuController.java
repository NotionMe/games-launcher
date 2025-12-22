package ua.notion.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import ua.notion.utils.WindowHandler;

public class SettingsMenuController {

  private static final String BASE_CSS = "/css/base.css";
  private static final String SETTINGS_MENU_CSS = "/css/settings-menu.css";

  @FXML
  private Button closeButton;
  @FXML
  private Button minButton;
  @FXML
  private Button fullButton;
  @FXML
  private Button pathButton;
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
  private void onPathButtonPressed(ActionEvent event) {
    System.out.println("Path button!");
  }

  @FXML
  private void onProtonsButtonPressed(ActionEvent event) {
    System.out.println("Proton button!");
  }

  @FXML
  private void onGraphicsButtonPressed(ActionEvent event) {
    System.out.println("Graphics button!");
  }

  @FXML
  private void onLauncherButtonPressed(ActionEvent event) {
    System.out.println("Launcher button!");
  }

  @FXML
  private void onAboutButtonPressed(ActionEvent event) {
    System.out.println("About button");
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
  }
}