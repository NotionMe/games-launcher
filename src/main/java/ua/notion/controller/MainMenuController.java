package ua.notion.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.services.GameService;

public class MainMenuController implements Initializable {

  private double xOffset = 0;
  private double yOffset = 0;

  private User user;

  private final UserData userData = new UserData();
  private final GameService gameService = new GameService(userData);

  @FXML
  private BorderPane rootPane;

  @FXML
  private Button buttonFull;
  @FXML
  private Button buttonMin;
  @FXML
  private Button buttonClose;
  @FXML
  private Button buttonAddGame;
  @FXML
  private Button buttonSettings;
  @FXML
  private Button buttonSupport;

  @FXML
  private Pane topPane;

  @FXML
  protected void handleCloseAction(ActionEvent e) {
    Stage stage = (Stage) buttonClose.getScene().getWindow();
    stage.close();
  }

  @FXML
  protected void handleMinAction(ActionEvent e) {
    Stage stage = (Stage) buttonMin.getScene().getWindow();
    stage.setIconified(true);
  }

  @FXML
  protected void handleFullAction(ActionEvent e) {
    Stage stage = (Stage) rootPane.getScene().getWindow();
    stage.setFullScreenExitHint("");

    if (stage.isFullScreen()) {
      stage.setFullScreen(false);
      rootPane.setStyle("-fx-background-color: #3D3D3D; -fx-background-radius: 20;");
    } else {
      stage.setFullScreen(true);
      rootPane.setStyle("-fx-background-color: #3D3D3D; -fx-background-radius: 0;");
    }
  }

  @FXML
  protected void handlePressAction(MouseEvent e) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      xOffset = stage.getX() - e.getScreenX();
      yOffset = stage.getY() - e.getScreenY();
    }
  }

  @FXML
  protected void handleMovementAction(MouseEvent e) {
    Stage stage = (Stage) rootPane.getScene().getWindow();

    if (!stage.isFullScreen()) {
      stage.setX(e.getScreenX() + xOffset);
      stage.setY(e.getScreenY() + yOffset);
    }
  }

  @FXML
  private void onAddGameButtonPressed(ActionEvent e) {

    Stage stage = (Stage) rootPane.getScene().getWindow();

    gameService.addGameFromFile(user, stage);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    user = userData.read();
  }
}