package ua.notion.services;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;

public class RemoveGameService {

  private static final Logger LOGGER = System.getLogger(RemoveGameService.class.getName());
  private final UserRepository userRepository;

  public RemoveGameService() {
    this.userRepository = new UserData();
  }

  public void removeGameFromLibrary(Game game) {
    User user = userRepository.findAll();
    user.removeGame(game);
    userRepository.save(user);

    GameService.refreshLibraryInMenu();
    LOGGER.log(INFO, "Game removed from library: " + game.title());
  }

  public CompletableFuture<Void> deleteDirectoryAsync(Path path, String description) {
    return CompletableFuture.runAsync(() -> {
      LOGGER.log(INFO, "Starting async deletion of " + description + ": " + path);
      try (var walk = Files.walk(path)) {
        walk.sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
        LOGGER.log(INFO, description + " deleted successfully.");
      } catch (IOException e) {
        LOGGER.log(ERROR, "Failed to delete " + description + "! " + e.getMessage());
        throw new RuntimeException("Deletion failed", e);
      }
    });
  }
}