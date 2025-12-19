package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
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

  public void createCardsOnFiles(DragEvent event, User user, FlowPane cardContainer,
      AnchorPane centerDropPane) {
    Dragboard db = event.getDragboard();
    boolean hasFile = db.hasFiles();

    if (hasFile && !db.getFiles().isEmpty()) {
      if (!extractArchiveFiles(event).isEmpty()) {
        Optional<Game> game = addGameFromArchive(user, extractArchiveFiles(event).get(0));
        game.ifPresent(g -> {
          try {
            createGameCard(g, cardContainer);
            hidePanelVisible(centerDropPane);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      }
    }
    event.consume();
    event.setDropCompleted(hasFile);
  }

  public void hidePanelVisible(AnchorPane anchorPane) {
    if (anchorPane.visibleProperty().get()) {
      anchorPane.setVisible(false);
      anchorPane.setManaged(false);
    }
  }

  public void showPanelVisible(AnchorPane anchorPane) {
    if (anchorPane.isVisible())
      return;

    anchorPane.setOpacity(0);
    anchorPane.setVisible(true);
    anchorPane.setManaged(true);

    FadeTransition ft = new FadeTransition(Duration.millis(250), anchorPane);
    ft.setFromValue(0);
    ft.setToValue(1);
    ft.play();
  }

  // filter for files
  public List<File> extractArchiveFiles(DragEvent event) {
    Dragboard db = event.getDragboard();
    List<File> archives = db.getFiles().stream().filter(File::isFile).filter(f -> {
      String n = f.getName().toLowerCase();
      return n.endsWith(".zip") || n.endsWith(".rar") || n.endsWith(".exe");
    }).collect(Collectors.toList());

    return archives;
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
