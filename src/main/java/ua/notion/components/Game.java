package ua.notion.components;

import ua.notion.utils.Constants.UI;

public record Game(String title, String targetPath, String iconPath, String coverPath) {

  public Game(String title, String targetPath) {
    this(title, targetPath, UI.DEFAULT_ICON_PATH, UI.DEFAULT_COVER_PATH);
  }
}
