package ua.notion.utils;

import java.io.File;
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
    public static final String SIDE_DRAWER_CSS = "/css/side-drawer.css";
    public static final String REMOVE_GAME_POPUP_CSS = "/css/remove-game-popup.css";
    public static final String REMOVE_GAME_ALERT_CSS = "/css/remove-game-alert.css";

    // Settings CSS
    public static final String GRAPHICS_PAGE_CSS = "/css/settings/graphics-page.css";
    public static final String LAUNCHER_PAGE_CSS = "/css/settings/launcher-page.css";
    public static final String PATHS_PAGE_CSS = "/css/settings/paths-page.css";
    public static final String PROTONS_PAGE_CSS = "/css/settings/protons-page.css";
    public static final String ABOUT_PAGE_CSS = "/css/settings/about-page.css";

    // Icons defaults
    public static final String DEFAULT_ICON_PATH = "/icons/default_photo_512x512.png";
    public static final String DEFAULT_COVER_PATH = "/icons/test_Image_background.jpg";

    // Fonts
    public static final String FONT_MAIN_PATH = "/fonts/FiraSans-Medium.ttf";
    public static final double FONT_DEFAULT_LOAD_SIZE = 15.0;

    private UI() {
      throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
  }

  public static final class Views {

    public static final String MAIN_MENU = "/ua/notion/main-menu.fxml";
    public static final String GAME_CARD = "/ua/notion/game-card.fxml";
    public static final String SETTINGS_MENU = "/ua/notion/settings-menu.fxml";
    public static final String SIDE_DRAWER = "/ua/notion/side-drawer.fxml";
    public static final String GAME_SELECT = "/ua/notion/game-select.fxml";
    public static final String SETTINGS_BASE_DIR = "/ua/notion/settings/";
    public static final String PROTON_ITEM = "/ua/notion/settings/proton-item.fxml";
    public static final String ARCHIVE_SELECTOR = "/ua/notion/archive-selector.fxml";
    public static final String REMOVE_GAME_POPUP = "/ua/notion/remove-game-popup.fxml";

    private Views() {
      throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
  }

  public static final class Data {

    public static final String USER_DB_FILE = "user.json";
    public static final String ICONS_DIR = "images";

    public static final File HOME_PATH = new File(System.getProperty("user.home"));
    public static final File SCRIPT_PATH = new File("src/main/resources/script/run-games.sh");
    public static final File PROTON_PATH =
        new File(Data.HOME_PATH, ".local/share/Steam/compatibilitytools.d/");
    public static final File PROTON_PATH_JSON = new File("proton.json");

    public static final List<String> SUPPORTED_EXTENSIONS_GAME = List.of("*.exe");
    public static final List<String> ARCHIVE_EXTENSIONS =
        List.of(".tar.gz", ".tar.xz", ".tar", ".rar", ".zip", ".7z", ".7zip");
    public static final List<String> SUPPORTED_EXTENSIONS_IMAGE = List.of("*.png", "*.jpg");

    private Data() {
      throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
  }
}
