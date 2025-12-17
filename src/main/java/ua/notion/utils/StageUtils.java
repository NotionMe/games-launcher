package ua.notion.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class StageUtils {

  private static final double SCREEN_SCALE = 0.60;

  private StageUtils() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static void configureScreenSize(Stage stage) {
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    stage.setWidth(bounds.getWidth() * SCREEN_SCALE);
    stage.setHeight(bounds.getHeight() * SCREEN_SCALE);

    stage.centerOnScreen();
  }
}
