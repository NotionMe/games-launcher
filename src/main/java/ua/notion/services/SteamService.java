package ua.notion.services;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.io.IOException;
import java.lang.System.Logger;
import java.util.Collections;
import ua.notion.utils.SteamPlatformUtils;

public class SteamService {

  private final SteamPlatformUtils steamUtils = new SteamPlatformUtils();

  private static final String STEAM_PROCESS = "steam";
  private static final Logger LOGGER = System.getLogger(SteamService.class.getName());

  public void launchSteam() {
    if (isSteamRunning()) {
      LOGGER.log(INFO, "Steam is already running.");
      return;
    }

    LOGGER.log(INFO, "Attempting to launch Steam...");

    String steamPath = steamUtils.resolveSteamExecutablePath();

    try {
      new ProcessBuilder(Collections.singletonList(steamPath)).start();

      boolean ready = waitForSteamReady();
      if (ready) {
        LOGGER.log(INFO, "Steam launched and user logged in successfully!");
      } else {
        LOGGER.log(WARNING, "Steam launch timeout (60s). Game might crash if Steam isn't ready.");
      }
    } catch (IOException e) {
      LOGGER.log(ERROR, "Error launching Steam: " + e.getMessage());
    }
  }

  private boolean isSteamRunning() {
    return ProcessHandle.allProcesses()
        .map(ProcessHandle::info)
        .flatMap(info -> info.command().stream())
        .map(String::toLowerCase)
        .anyMatch(cmd -> cmd.contains(STEAM_PROCESS));
  }

  private boolean waitForSteamReady() {
    int count = 60;
    LOGGER.log(INFO, "Waiting for Steam login...");

    while (count > 0) {
      if (steamUtils.isSteamLoggedIn()) {
        return true;
      }
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return false;
      }
      count--;
    }
    return false;
  }
}