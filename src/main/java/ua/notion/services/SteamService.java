package ua.notion.services;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.io.IOException;
import java.lang.System.Logger;
import java.util.Collections;
import ua.notion.utils.OsUtils;
import ua.notion.utils.SteamPlatformUtils;

public class SteamService {

  private final SteamPlatformUtils steamUtils = new SteamPlatformUtils();

  private static final String STEAM_PROCESS_LINUX = "steam";
  private static final String STEAM_PROCESS_WIN = "steam.exe";

  private static final Logger LOGGER = System.getLogger(SteamService.class.getName());

  public void launchSteam() {
    if (!isSteamRunning()) {
      LOGGER.log(INFO, "Attempting to launch Steam...");

      String steamPath = steamUtils.resolveSteamExecutablePath();

      if (steamPath == null || steamPath.equals(STEAM_PROCESS_LINUX) && OsUtils.isWindows()) {
        LOGGER.log(WARNING, "Steam path could not be resolved from registry.");
      }

      try {
        new ProcessBuilder(Collections.singletonList(steamPath)).start();
      } catch (IOException e) {
        LOGGER.log(ERROR, "Error launching Steam: " + e.getMessage());
        return;
      }
    } else {
      LOGGER.log(INFO, "Steam is already running, verifying readiness...");
    }

    boolean ready = waitForSteamReady();
    if (ready) {
      LOGGER.log(INFO, "Steam launched and user logged in successfully!");
    } else {
      LOGGER.log(WARNING, "Steam launch timeout (60s). Game might crash if Steam isn't ready.");
    }
  }

  private boolean isSteamRunning() {
    String targetProcess = OsUtils.isWindows() ? STEAM_PROCESS_WIN : STEAM_PROCESS_LINUX;

    return ProcessHandle.allProcesses()
        .map(ProcessHandle::info)
        .flatMap(info -> info.command().stream())
        .map(String::toLowerCase)
        .anyMatch(
            cmd -> OsUtils.isWindows() ? cmd.endsWith(targetProcess) : cmd.contains(targetProcess));
  }

  private boolean waitForSteamReady() {
    int attempts = 60;
    LOGGER.log(INFO, "Waiting for Steam login...");

    while (attempts > 0) {
      if (steamUtils.isSteamLoggedIn()) {
        try {
          Thread.sleep(OsUtils.isLinux() ? 2000 : 500);
          return true;
        } catch (InterruptedException e) {
          LOGGER.log(ERROR, "Steam stabilization wait interrupted");
          Thread.currentThread().interrupt();
          return false;
        }
      }
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        LOGGER.log(ERROR, "Steam polling loop interrupted");
        Thread.currentThread().interrupt();
        return false;
      }
      attempts--;
    }
    return false;
  }
}