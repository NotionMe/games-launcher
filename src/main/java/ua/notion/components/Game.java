package ua.notion.components;

import java.util.Objects;

public class Game {

  private String title;
  private String targetPath;
  private String launchArguments;
  private String pfx;
  private String dllWineOverride;
  private String defaultProtonVersion;
  private String iconPath;
  private String coverPath;

  public Game(String title, String targetPath, String launchArguments, String pfx,
      String dllWineOverride, String defaultProtonVersion, String iconPath, String coverPath) {
    this.title = title;
    this.targetPath = targetPath;
    this.launchArguments = launchArguments;
    this.pfx = pfx;
    this.dllWineOverride = dllWineOverride;
    this.defaultProtonVersion = defaultProtonVersion;
    this.iconPath = iconPath;
    this.coverPath = coverPath;
  }

  public String getTitle() {
    return title;
  }

  public String getTargetPath() {
    return targetPath;
  }

  public String getLaunchArguments() {
    return launchArguments;
  }

  public String getPfx() {
    return pfx;
  }

  public String getDllWineOverride() {
    return dllWineOverride;
  }

  public String getDefaultProtonVersion() {
    return defaultProtonVersion;
  }

  public String getIconPath() {
    return iconPath;
  }

  public String getCoverPath() {
    return coverPath;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPfx(String pfx) {
    this.pfx = pfx;
  }

  public void setTargetPath(String targetPath) {
    this.targetPath = targetPath;
  }

  public void setLaunchArguments(String launchArguments) {
    this.launchArguments = launchArguments;
  }

  public void setDllWineOverride(String dllWineOverride) {
    this.dllWineOverride = dllWineOverride;
  }

  public void setDefaultProtonVersion(String defaultProtonVersion) {
    this.defaultProtonVersion = defaultProtonVersion;
  }

  public void setIconPath(String iconPath) {
    this.iconPath = iconPath;
  }

  public void setCoverPath(String coverPath) {
    this.coverPath = coverPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Game game = (Game) o;

    return Objects.equals(title, game.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }
}
