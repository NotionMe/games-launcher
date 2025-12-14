package ua.notion.services;

import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserRepository;

public class GameService {

  private final UserRepository repository;

  public GameService(UserRepository repository) {
    this.repository = repository;
  }

  public void addGameFromFile(User user, Window parentWindow) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters()
        .addAll(new ExtensionFilter("Game files", "*.exe", "*.zip", "*.rar"));

    File file = fileChooser.showOpenDialog(parentWindow);

    if (file != null) { // TODO we have zip and rar, if zip or rar we need unpacking it and etc.
      String path = file.getPath();
      String name = file.getName();

      Game newGame = new Game(name, path);
      user.addGame(newGame);

      repository.write(user);
    }
  }
}
