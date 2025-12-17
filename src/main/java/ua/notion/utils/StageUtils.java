package ua.notion.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class StageUtils {

  public static void configurateStage(Stage stage) {
    Screen screen = Screen.getPrimary();

    Rectangle2D bounds = screen.getVisualBounds();

    stage.setWidth(bounds.getWidth() * 0.60);
    stage.setHeight(bounds.getHeight() * 0.60);
  }
}
