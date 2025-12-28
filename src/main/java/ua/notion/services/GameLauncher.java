package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ua.notion.components.Game;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.OsUtils;

public class GameLauncher {

  // TODO костиль! може бути диск D І Т.П.
  private static final String STEAM_PATH_WIN = "C:\\Program Files (x86)\\Steam\\steam.exe";
  private static final String STEAM_PROCESS = "steam";

  private static final System.Logger LOGGER = System.getLogger(GameLauncher.class.getName());

  public void play(Game game) {
    launchSteam();
    if (OsUtils.isWindows()) {
      launchWindows(game);
    } else {
      launchLinux(game);
    }
  }

  private void launchLinux(Game game) {
    String script = Data.SCRIPT_PATH.getAbsolutePath();
    String protonPath = game.defaultProtonVersion();

    List<String> command = new ArrayList<>();
    command.add(script);
    command.add(protonPath);
    command.add(game.targetPath());
    command.add(game.pfx());
    command.add(game.dllWineOverride());

    addArguments(command, game.launchArguments());

    executeProcess(command, game);
  }

  private void launchWindows(Game game) {
    List<String> command = new ArrayList<>();
    command.add(game.targetPath());

    addArguments(command, game.launchArguments());

    executeProcess(command, game);
  }

  private void executeProcess(List<String> command, Game game) {
    if (game.targetPath() == null) {
      LOGGER.log(System.Logger.Level.ERROR, "Error: Game target path is null");
      return;
    }

    File workDir = new File(game.targetPath()).getParentFile();

    LOGGER.log(System.Logger.Level.DEBUG, "DEBUG COMMAND: " + command);

    try {
      ProcessBuilder pb = new ProcessBuilder(command);
      pb.directory(workDir);
      pb.inheritIO();

      Process process = pb.start();

      LOGGER.log(System.Logger.Level.INFO, "Started game: " + game.title());

      process.onExit().thenAccept(p -> {
        LOGGER.log(System.Logger.Level.INFO,
            "Game closed: " + game.title() + " (Exit code: " + p.exitValue() + ")");
      });
    } catch (IOException e) {
      LOGGER.log(System.Logger.Level.ERROR,
          "Error launching " + game.title() + ": " + e.getMessage(), e);
      e.printStackTrace();
    }
  }

  private void addArguments(List<String> command, String arguments) {
    if (arguments == null || arguments.isBlank()) {
      return;
    }

    Matcher matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(arguments);
    while (matcher.find()) {
      command.add(matcher.group(1).replace("\"", ""));
    }
  }

  public void launchSteam() { // todo remove public???
    if (isSteamRunning()) {
      LOGGER.log(System.Logger.Level.INFO, "Steam is already running.");
      return;
    }

    LOGGER.log(System.Logger.Level.INFO, "Attempting to launch Steam...");
    List<String> command = new ArrayList<>();

    if (OsUtils.isWindows()) {
      // TODO тут той самий костиль
      command.add(STEAM_PATH_WIN);
    } else {
      command.add(STEAM_PROCESS);
    }
    try {
      new ProcessBuilder(command).start();
      LOGGER.log(System.Logger.Level.INFO, "Steam launch command sent!");
    } catch (IOException e) {
      LOGGER.log(System.Logger.Level.ERROR, "Error launching Steam: " + e.getMessage());
    }
  }

  private boolean isSteamRunning() {
    return isProcessRunning(STEAM_PROCESS);
  }

  private boolean isProcessRunning(String processName) {
    return ProcessHandle.allProcesses()
        .map(ProcessHandle::info)
        .flatMap(info -> info.command().stream())
        .map(String::toLowerCase)
        .anyMatch(cmd -> cmd.contains(processName.toLowerCase()));
  }
}