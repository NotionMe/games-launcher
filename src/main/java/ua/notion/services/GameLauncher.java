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

  private static final String STEAM_PATH_WIN = "C:\\Program Files (x86)\\Steam\\steam.exe";
  private static final String STEAM_PROCESS = "steam";
  private static final System.Logger LOGGER = System.getLogger(GameLauncher.class.getName());

  public GameLauncher() { // TODO тимчасово public, private по дефолту
  }

  public static void play(Game game) {
    if (OsUtils.isWindows()) {
      launchWindows(game);
    } else {
      launchLinux(game);
    }
  }

  private static void launchLinux(Game game) {
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

  private static void launchWindows(Game game) {
    List<String> command = new ArrayList<>();
    command.add(game.targetPath());

    addArguments(command, game.launchArguments());

    executeProcess(command, game);
  }

  private static void executeProcess(List<String> command, Game game) {
    if (game.targetPath() == null) {
      System.err.println("Error: Game target path is null");
      return;
    }

    File workDir = new File(game.targetPath()).getParentFile();

    System.out.println("DEBUG COMMAND: " + command);

    try {
      ProcessBuilder pb = new ProcessBuilder(command);
      pb.directory(workDir);
      pb.inheritIO();

      Process process = pb.start();

      System.out.println("Started game: " + game.title());

      process.onExit().thenAccept(p -> {
        System.out.println("Game closed: " + game.title());
        System.out.println("Exit code: " + p.exitValue());
      });
    } catch (IOException e) {
      System.err.println("Error launching " + game.title() + ": " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void addArguments(List<String> command, String arguments) {
    if (arguments == null || arguments.isBlank()) {
      return;
    }

    Matcher matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(arguments);
    while (matcher.find()) {
      command.add(matcher.group(1).replace("\"", ""));
    }
  }

  public void launchSteam() { // todo поки що для вінди хоч
    if (isSteamRunning()) {
      System.out.println("STEAM ALREADY RUNNING");
      return;
    }
    try {
      if(OsUtils.isWindows()) {
        Runtime.getRuntime().exec(STEAM_PATH_WIN);
      }
      else if(OsUtils.isLinux() || OsUtils.isMac()){
        Runtime.getRuntime().exec(STEAM_PROCESS);
      }
      System.out.println("Steam launch command sent!");
    } catch (IOException e) {
      System.out.println("Error! Steam not found! " + e);
    }
  }

  public boolean isSteamRunning() { // TODO ТИМЧАСОВО PUBLIC
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