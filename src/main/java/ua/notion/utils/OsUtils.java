package ua.notion.utils;

import static java.lang.System.Logger.Level.ERROR;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import java.util.concurrent.CompletableFuture;

public final class OsUtils {

  public enum OS {
    WINDOWS,
    LINUX,
    MAC,
    OTHER
  }

  private static final OS CURRENT_OS = determineOS();
  private static final Logger LOGGER = System.getLogger(OsUtils.class.getName());

  private OsUtils() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  private static OS determineOS() {
    String osName = System.getProperty("os.name", "generic").toLowerCase();

    if (osName.contains("win")) {
      return OS.WINDOWS;
    } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
      return OS.LINUX;
    } else if (osName.contains("mac")) {
      return OS.MAC;
    } else {
      return OS.OTHER;
    }
  }

  public static void openPath(File file) {
    if (file == null || !file.exists()) {
      LOGGER.log(ERROR, "Path is null or does not exist");
      return;
    }

    CompletableFuture.runAsync(() -> {
      try {
        if (isLinux()) {
          new ProcessBuilder("xdg-open", file.getAbsolutePath()).start();
        } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.OPEN)) {
          Desktop.getDesktop().open(file);
        } else {
          LOGGER.log(ERROR, "Opening files is not supported on this platform.");
        }
      } catch (IOException e) {
        LOGGER.log(ERROR, "Failed to open path: " + e.getMessage(), e);
      }
    });
  }

  public static OS getCurrentOs() {
    return CURRENT_OS;
  }

  public static boolean isWindows() {
    return CURRENT_OS == OS.WINDOWS;
  }

  public static boolean isMac() {
    return CURRENT_OS == OS.MAC;
  }

  public static boolean isLinux() {
    return CURRENT_OS == OS.LINUX;
  }

  public static boolean isOther() {
    return CURRENT_OS == OS.OTHER;
  }
}