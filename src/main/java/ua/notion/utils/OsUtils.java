package ua.notion.utils;

public final class OsUtils {

  public enum OS {
    WINDOWS,
    LINUX,
    MAC,
    OTHER
  }

  private static final OS CURRENT_OS = determineOS();

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