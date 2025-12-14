package ua.notion.utils;

public final class OsUtils {

  private static final String OS = System.getProperty("os.name", "generic").toLowerCase();

  public static final boolean IS_WINDOWS = OS.contains("win");
  public static final boolean IS_LINUX = OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
  public static final boolean IS_MAC = OS.contains("mac");

  private OsUtils() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  public static boolean isWindows() {
    return IS_WINDOWS;
  }

  public static boolean isLinux() {
    return IS_LINUX;
  }

  public static boolean isMac() {
    return IS_MAC;
  }
}