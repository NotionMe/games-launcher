package ua.notion.components;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

  private final String id;
  private final UserPaths paths = new UserPaths();
  private final UserLauncher launcherSettings = new UserLauncher();
  private final List<Game> library = new ArrayList<>();

  public User() {
    this(UUID.randomUUID().toString());
  }

  public User(String id) {
    this.id = id;
  }

  public void addGame(Game game) {
    library.add(game);
  }

  public List<Game> getLibrary() {
    return library;
  }

  public void removeGame(Game game) {
    library.remove(game);
  }

  public String getId() {
    return id;
  }

  public UserPaths getPaths() {
    return paths;
  }

  public UserLauncher getLauncherSettings(){
    return launcherSettings;
  }
}
