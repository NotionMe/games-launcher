package ua.notion.components;

public class Game {

  private final String title;
  private final String targetPath;
  private String iconPath; // can be null
  private String coverPath; // can be null

  public Game(String title, String targetPath) {
    this.title = title;
    this.targetPath = targetPath; // path to our game
    this.iconPath = "/icons/default_photo_512x512.png";
    this.coverPath = "/icons/test_Image_background.jpg";
  }

  public Game(String title, String targetPath, String iconPath, String coverPath) {
    this.title = title;
    this.targetPath = targetPath;
    this.iconPath = iconPath;
    this.coverPath = coverPath;
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

  public String getCoverPath() {
    return coverPath;
  }

  public void setCoverPath(String coverPath) {
    this.coverPath = coverPath;
  }
}
