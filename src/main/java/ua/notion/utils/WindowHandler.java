package ua.notion.utils;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class WindowHandler {

  private final Pane rootPane;
  private double xOffset = 0;
  private double yOffset = 0;

  public WindowHandler(Pane rootPane) {
    this.rootPane = rootPane;
  }

  public void initHandler(Stage stage) {
    stage.setFullScreenExitHint("");

    stage.fullScreenProperty().addListener((obs, wasFull, isFull) -> {
      updateStyle(isFull);
    });

    if (stage.isFullScreen()) {
      updateStyle(true);
    }
  }

  public void close(Node source) {
    getStage(source).close();
  }

  public void minimize(Node source) {
    getStage(source).setIconified(true);
  }

  public void toggleFullscreen(Node source) {
    Stage stage = getStage(source);

    stage.setFullScreen(!stage.isFullScreen());
  }

  public void onPress(MouseEvent event) {
    Stage stage = getStage(rootPane);
    if (!stage.isFullScreen()) {
      xOffset = stage.getX() - event.getScreenX();
      yOffset = stage.getY() - event.getScreenY();
    }
  }

  public void onDrag(MouseEvent event) {
    Stage stage = getStage(rootPane);
    if (!stage.isFullScreen()) {
      stage.setX(event.getScreenX() + xOffset);
      stage.setY(event.getScreenY() + yOffset);
    }
  }

  private void updateStyle(boolean isFull) {
    if (isFull) {
      if (!rootPane.getStyleClass().contains("fullscreen")) {
        rootPane.getStyleClass().add("fullscreen");
      }
    } else {
      rootPane.getStyleClass().remove("fullscreen");
    }
  }

  private Stage getStage(Node node) {
    return (Stage) node.getScene().getWindow();
  }
}