package ua.notion.components;

public record Game(String title, String targetPath, String iconPath, String coverPath) {

  private static final String DEFAULT_ICON_PATH = "/icons/default_photo_512x512.png";
  private static final String DEFAULT_COVER_PATH = "/icons/test_Image_background.jpg";


  public Game(String title, String targetPath) {
    this(title, targetPath, DEFAULT_ICON_PATH, DEFAULT_COVER_PATH);
  }
}
