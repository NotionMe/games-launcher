package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.Window;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.controllers.GameCardController;
import ua.notion.controllers.MainMenuController;
import ua.notion.controllers.SideDrawerController;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.Constants.Views;

public class GameService {

  private final UserRepository repository;
  private final IconService iconService;
  private static MainMenuController mainMenuController;


  public GameService(UserRepository repository, IconService iconService) {
    this.repository = repository;
    this.iconService = iconService;
  }

  public static void setMainMenuController(MainMenuController mainMenuController) {
    GameService.mainMenuController = mainMenuController;
  }


  public File getFilePath(List<String> data, String description) {
    Stage stage = (Stage) mainMenuController.getRootPane().getScene().getWindow();
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter(description, data));
    return fileChooser.showOpenDialog(stage);
  }

  public Optional<Game> addGameFromArchive(User user, File file) {
    if (file == null) {
      return Optional.empty();
    }

    String name = file.getName();
    String path = file.getPath();

    Game game = new Game(name, path, "", "", "", "");
    user.addGame(game);

    repository.save(user);
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

  public static void showPanelVisible(AnchorPane anchorPane) {
    if (anchorPane.isVisible()) {
      return;
    }

    anchorPane.setOpacity(0);
    anchorPane.setVisible(true);
    anchorPane.setManaged(true);

    FadeTransition ft = new FadeTransition(Duration.millis(250), anchorPane);
    ft.setFromValue(0);
    ft.setToValue(1);
    ft.play();
  }

  public void loadGameCards(User user, AnchorPane anchorPane, FlowPane flowPane) {
    if (UserData.fileIsExists()) {
      hidePanelVisible(anchorPane);
      List<Game> games = user.getLibrary();
      for (Game game : games) {
        try {
          createGameCard(game, flowPane);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
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
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Views.GAME_CARD));
    Node card = fxmlLoader.load();
    GameCardController gameCardController = fxmlLoader.getController();
    gameCardController.setGame(game);
    cardContainer.getChildren().add(card);
  }

  public static File[] getProtonVersionHost(String data) {
    File directory = new File(data);
    if (!directory.exists() || !directory.isDirectory()) {
      return new File[0];
    }
    File[] files = directory.listFiles((dir, name) -> {
      File file = new File(dir, name);
      return file.isDirectory() && name.toLowerCase().startsWith("ge-proton");
    });
    return files != null ? files : new File[0];
  }
}
