package ua.notion.components;

import java.io.File;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.Constants.UI;

public record Game(String title, String targetPath, String iconPath, String coverPath) {

  public Game(String title, String targetPath) {
    this(title, targetPath, UI.DEFAULT_ICON_PATH, UI.DEFAULT_COVER_PATH);
  }

  public Game(String title, String targetPath, File file) {
    this(title, targetPath, Data.SCRIPT_PATH.getAbsolutePath(), UI.DEFAULT_COVER_PATH);
  }
}
