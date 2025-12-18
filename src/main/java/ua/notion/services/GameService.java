package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.controller.GameCardController;
import ua.notion.data.UserRepository;

public class GameService {

  private final UserRepository repository;
  private final IconService iconService;

  public GameService(UserRepository repository, IconService iconService) {
    this.repository = repository;
    this.iconService = iconService;
  }

  public Optional<Game> addGameFromFile(User user, Window parentWindow) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters()
        .addAll(new ExtensionFilter("Game files", "*.exe", "*.zip", "*.rar"));

    File file = fileChooser.showOpenDialog(parentWindow);

    if (file == null) {
      return Optional.empty();
    }

    // TODO: handle archives (zip, rar) before storing them.
    String path = file.getPath();
    String name = file.getName();

    Game newGame = new Game(name, path);
    user.addGame(newGame);

    repository.write(user);
    return Optional.of(newGame);
  }

  public Optional<Game> addGameFromArchive(User user, File file) {
    if (file == null)
      return Optional.empty();

    String name = file.getName();
    String path = file.getPath();

    Game game = new Game(name, path);
    user.addGame(game);

    repository.write(user);
    return Optional.of(game);
  }


  public void createGameCard(Game game, FlowPane cardContainer) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ua/notion/game-card.fxml"));
    Node card = fxmlLoader.load();
    GameCardController gameCardController = fxmlLoader.getController();
    gameCardController.setCover(game.getCoverPath());
    gameCardController.setIcon(game.getIconPath());
    gameCardController.setTitle(game.getTitle());
    cardContainer.getChildren().add(card);
  }
}
