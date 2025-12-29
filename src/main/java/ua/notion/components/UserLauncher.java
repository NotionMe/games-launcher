package ua.notion.components;

public class UserLauncher {

  private boolean isFullScreen = false;

  private boolean isSteamDisabled = false;

  private boolean isAnimationDisabled = false;


  public boolean isFullScreen() {
    return isFullScreen;
  }

  public boolean isSteamDisabled() {
    return isSteamDisabled;
  }

  public boolean isAnimationDisabled() {
    return isAnimationDisabled;
  }

  public void setFullScreen(boolean isFullScreen){
    this.isFullScreen = isFullScreen;
  }

  public void setSteamDisabled(boolean isSteamDisabled){
    this.isSteamDisabled = isSteamDisabled;
  }

  public void setAnimationDisabled(boolean isAnimationDisabled){
    this.isAnimationDisabled = isAnimationDisabled;
  }
}