package ua.notion.components;

public class Game {

  private final String title;
  private final String targetPath;
  private String iconPath; // can be null

  public Game(String title, String targetPath) {
    this.title = title;
    this.targetPath = targetPath;  // path to our game
    this.iconPath = "/icons/default_photo_512x512.png";
  }

  public Game(String title, String targetPath, String iconPath) {
    this.title = title;
    this.targetPath = targetPath;
    this.iconPath = iconPath;
  }

  public String getTitle() {
    return title;
  }

  public String getTargetPath() {
    return targetPath;
  }

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath;
  }
}
