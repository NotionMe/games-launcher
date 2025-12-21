package ua.notion.utils;

import java.util.List;

public final class Constants {

  private Constants() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static final class UI {

    public static final String APP_TITLE = "Games launcher";
    public static final double SCREEN_SCALE = 0.60;

    // CSS
    public static final String BASE_CSS = "/css/base.css";
    public static final String MAIN_MENU_CSS = "/css/main-menu.css";
    public static final String SETTINGS_MENU_CSS = "/css/settings-menu.css";

    // Icons defaults
    public static final String DEFAULT_ICON_PATH = "/icons/default_photo_512x512.png";
    public static final String DEFAULT_COVER_PATH = "/icons/test_Image_background.jpg";
  }

  public static final class Views {

    public static final String MAIN_MENU = "/ua/notion/main-menu.fxml";
    public static final String GAME_CARD = "/ua/notion/game-card.fxml";
    public static final String SETTINGS_MENU = "/ua/notion/settings-menu.fxml";
  }

  public static final class Data {

    public static final String USER_DB_FILE = "user.json";
    public static final String ICONS_DIR = "images";

    public static final List<String> SUPPORTED_EXTENSIONS = List.of("*.exe", "*.zip", "*.rar");
  }
}