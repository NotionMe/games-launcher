package ua.notion.services;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.OsUtils;

public class GameLauncher {

  private final SteamService steamService = new SteamService();

  private static final Pattern ARG_PATTERN = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
  private static final Logger LOGGER = System.getLogger(GameLauncher.class.getName());

  public void play(Game game, User user) {
    if (!user.getLauncherSettings().isSteamDisabled()) {
      steamService.launchSteam();
    } else {
      LOGGER.log(INFO, "Auto steam launch is disabled via settings");
    }
    if (OsUtils.isWindows()) {
      launchWindows(game);
    } else {
      launchLinux(game);
    }
  }

  private void launchLinux(Game game) {
    String script = Data.SCRIPT_PATH.getAbsolutePath();
    String protonPath = game.getDefaultProtonVersion();

    List<String> command = new ArrayList<>();
    command.add(script);
    command.add(protonPath);
    command.add(game.getTargetPath());
    command.add(game.getPfx());
    command.add(game.getDllWineOverride());

    addArguments(command, game.getLaunchArguments());

    executeProcess(command, game);
  }

  private void launchWindows(Game game) {
    List<String> command = new ArrayList<>();
    command.add(game.getTargetPath());

    addArguments(command, game.getLaunchArguments());

    executeProcess(command, game);
  }

  private void executeProcess(List<String> command, Game game) {
    if (game.getTargetPath() == null) {
      LOGGER.log(ERROR, "Error: Game target path is null");
      return;
    }

    File workDir = new File(game.getTargetPath()).getParentFile();

    LOGGER.log(DEBUG, "DEBUG COMMAND: " + command);

    try {
      ProcessBuilder pb = new ProcessBuilder(command);
      pb.directory(workDir);
      pb.inheritIO();

      Process process = pb.start();

      LOGGER.log(INFO, "Started game: " + game.getTitle());

      process.onExit().thenAccept(p ->
        LOGGER.log(INFO,
            "Game closed: " + game.getTitle() + " (Exit code: " + p.exitValue() + ")"));
    } catch (IOException e) {
      LOGGER.log(ERROR,
          "Error launching " + game.getTitle() + ": " + e.getMessage(), e);
    }
  }

  private void addArguments(List<String> command, String arguments) {
    if (arguments == null || arguments.isBlank()) {
      return;
    }

    Matcher matcher = ARG_PATTERN.matcher(arguments);
    while (matcher.find()) {
      command.add(matcher.group(1).replace("\"", ""));
    }
  }
}