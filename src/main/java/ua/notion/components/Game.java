package ua.notion.components;

public class Game {

  private final String title;
  private final String exePath;
  private String iconPath; // can be null

  public Game(String title, String exePath) {
    this.title = title;
    this.exePath = exePath;
    this.iconPath = "default_photo_512x512.png";
  }

  public Game(String title, String exePath, String iconPath) {
    this.title = title;
    this.exePath = exePath;
    this.iconPath = iconPath;
  }

  public String getTitle() {
    return title;
  }

  public String getExePath() {
    return exePath;
  }

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath;
  }
}
