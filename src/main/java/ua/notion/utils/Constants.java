package ua.notion.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    // Icons defaults
    public static final String DEFAULT_ICON_PATH = "/icons/default_photo_512x512.png";
    public static final String DEFAULT_COVER_PATH = "/icons/test_Image_background.jpg";
  }

  public static final class Views {

    public static final String MAIN_MENU = "/ua/notion/main-menu.fxml";
    public static final String GAME_CARD = "/ua/notion/game-card.fxml";
    public static final String SETTINGS_MENU = "/ua/notion/settings-menu.fxml";
    public static final String SIDE_DRAWER = "/ua/notion/side-drawer.fxml";
    public static final String GAME_SELECT = "/ua/notion/game-select.fxml";
  }

  public static final class Data {

    public static final String USER_DB_FILE = "user.json";
    public static final String ICONS_DIR = "images";

    // home linux (Поки не трогати недороблено!)
    public static final File HOME_PATH = new File(System.getProperty("user.home"));
    public static final File SCRIPT_PATH = new File("src/main/resources/script/run-games.sh");
    public static final File PROTON_PATH =
        new File(Data.HOME_PATH, ".local/share/Steam/compatibilitytools.d/");
    // public static final File EXE_PATH = new File("/mnt/data/game/Bodycam/Bodycam.exe");
    // public static final Path PREFIX_PATH = Paths.get(Data.HOME_PATH + "/Games/pfx");
    // public static final String WINEDLLOVERRIDES =
    // "onlinefix64=n;winmm=n,b;steam_api64=n;eossdk-win64-shipping=n";

    public static final List<String> SUPPORTED_EXTENSIONS_GAME = List.of("*.exe");
    public static final List<String> SUPPORTED_EXTENSIONS_ARCHIVE =
        List.of("*.zip", "*.rar", "*.7z", "*.tar", "*.gz");
    public static final List<String> SUPPORTED_EXTENSIONS_IMAGE = List.of("*.png", "*.jpg");
  }
}
