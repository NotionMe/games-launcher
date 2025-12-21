package ua.notion.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SettingsMenuController {

  private static final String BASE_CSS = "/css/base.css";
  private static final String SETTINGS_MENU_CSS = "/css/settings-menu.css";

  private double xOffset = 0;
  private double yOffset = 0;

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
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  protected void handleMinAction(ActionEvent event) {
    Stage stage = (Stage) minButton.getScene().getWindow();
    stage.setIconified(true);
  }

  @FXML
  protected void handleFullAction(ActionEvent event) {
    Stage stage = (Stage) rootPane.getScene().getWindow();
    stage.setFullScreenExitHint("");

    if (stage.isFullScreen()) {
      stage.setFullScreen(false);
      rootPane.getStyleClass().remove("fullscreen");
    } else {
      stage.setFullScreen(true);
      rootPane.getStyleClass().add("fullscreen");
    }
  }

  @FXML
  protected void handlePressAction(MouseEvent event) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      xOffset = stage.getX() - event.getScreenX();
      yOffset = stage.getY() - event.getScreenY();
    }
  }

  @FXML
  protected void handleMovementAction(MouseEvent event) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      stage.setX(event.getScreenX() + xOffset);
      stage.setY(event.getScreenY() + yOffset);
    }
  }

  @FXML
  private void initialize() {

    rootPane.getStylesheets().addAll(
        getClass().getResource(SETTINGS_MENU_CSS).toExternalForm(),
        getClass().getResource(BASE_CSS).toExternalForm()
    );
  }
}