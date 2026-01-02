package ua.notion.utils;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.util.List;

public class SteamPlatformUtils {

  private static final Logger LOGGER = System.getLogger(SteamPlatformUtils.class.getName());

  private static final String REG_TOKEN = "REG_SZ";
  private static final String WIN_ACTIVE_USER_KEY = "ActiveUser";
  private static final String LINUX_ACTIVE_PROCESS_KEY = "SteamPID";

  private static final List<String> LINUX_STEAM_REGISTRY_PATHS =
      List.of("/.steam/steam/registry.vdf", "/.local/share/Steam/registry.vdf",
          "/.var/app/com.valvesoftware.Steam/.steam/steam/registry.vdf", "/.steam/registry.vdf");

  public String resolveSteamExecutablePath() {
    if (!OsUtils.isWindows()) {
      return "steam";
    }

    String hkcu = getRegistryValue("HKEY_CURRENT_USER\\Software\\Valve\\Steam", "SteamExe");
    if (hkcu != null) {
      return new File(hkcu).getAbsolutePath();
    }

    String hklm =
        getRegistryValue("HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Valve\\Steam", "InstallPath");
    if (hklm != null) {
      return new File(hklm, "steam.exe").getAbsolutePath();
    }

    String hklm32 = getRegistryValue("HKEY_LOCAL_MACHINE\\SOFTWARE\\Valve\\Steam", "InstallPath");
    if (hklm32 != null) {
      return new File(hklm32, "steam.exe").getAbsolutePath();
    }

    LOGGER.log(WARNING, "Steam path not found in Registry. Using fallback 'steam'.");
    return "steam";
  }

  public boolean isSteamLoggedIn() {
    if (OsUtils.isWindows()) {
      return checkWindowsRegistryLogin();
    }
    if (OsUtils.isLinux()) {
      return checkLinuxFileLogin();
    }
    return false;
  }

  private boolean checkWindowsRegistryLogin() {
    String hexValue = getRegistryValue("HKEY_CURRENT_USER\\Software\\Valve\\Steam\\ActiveProcess",
        WIN_ACTIVE_USER_KEY);

    if (hexValue != null) {
      String cleanValue = hexValue.trim();
      return !cleanValue.equals("0x0") && !cleanValue.equals("0");
    }
    return false;
  }

  private boolean checkLinuxFileLogin() {
    String userHome = System.getProperty("user.home");
    File registryFile = null;

    for (String path : LINUX_STEAM_REGISTRY_PATHS) {
      File f = new File(userHome + path);
      if (f.exists()) {
        registryFile = f;
        break;
      }
    }

    if (registryFile == null) {
      LOGGER.log(WARNING, "Linux Steam registry file not found in known locations.");
      return false;
    }

    return parseLinuxVdfForActiveUser(registryFile);
  }

  private boolean parseLinuxVdfForActiveUser(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains(LINUX_ACTIVE_PROCESS_KEY)) {
          String[] parts = line.trim().split("\\s+");
          if (parts.length >= 2) {
            String value = parts[1].replace("\"", "");
            return !value.equals("0") && !value.equals("0x0");
          }
        }
      }
    } catch (IOException e) {
      LOGGER.log(ERROR, "Failed to read Linux registry file: " + e.getMessage());
    }
    return false;
  }

  private String getRegistryValue(String path, String key) {
    try {
      ProcessBuilder pb = new ProcessBuilder("REG", "QUERY", path, "/v", key);
      pb.redirectErrorStream(true);
      Process process = pb.start();

      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.contains(key)) {
            if (line.contains(REG_TOKEN)) {
              int index = line.indexOf(REG_TOKEN);
              return line.substring(index + REG_TOKEN.length()).trim();
            } else {
              String[] parts = line.trim().split("\\s+");
              if (parts.length > 0) {
                return parts[parts.length - 1];
              }
            }
          }
        }
      }
    } catch (IOException e) {
      LOGGER.log(ERROR, "Registry check failed for " + path + ": " + e.getMessage());
    }
    return null;
  }
}
