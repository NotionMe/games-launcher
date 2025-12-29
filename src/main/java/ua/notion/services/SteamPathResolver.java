package ua.notion.services;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.System.Logger;

public class SteamPathResolver {

  private static final Logger LOGGER = System.getLogger(SteamPathResolver.class.getName());

  public String resolveWindowsSteamPath() {
    String HKCU = getSteamPath("HKEY_CURRENT_USER\\Software\\Valve\\Steam", "SteamExe");
    if (HKCU != null) {
      return new File(HKCU).getAbsolutePath();
    }
    String HKLM = getSteamPath("HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Valve\\Steam",
        "InstallPath");
    if (HKLM != null) {
      return new File(HKLM, "steam.exe").getAbsolutePath();
    }
    String HKLM_32 = getSteamPath("HKEY_LOCAL_MACHINE\\SOFTWARE\\Valve\\Steam",
        "InstallPath");
    if (HKLM_32 != null) {
      return new File(HKLM_32, "steam.exe").getAbsolutePath();
    }
    LOGGER.log(WARNING, "Steam path not found in Windows Registry. Trying fallback command 'steam'.");
    return "steam";
  }

  private String getSteamPath(String path, String key) {
    try {
      ProcessBuilder pb = new ProcessBuilder("REG", "QUERY", path, "/v", key);

      Process process = pb.start();

      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.contains("REG_SZ")) {
            return line.substring(line.indexOf("REG_SZ") + 6).trim();
          }
        }
      }
    } catch (IOException e) {
      LOGGER.log(ERROR, "Failed to read registry path: " + path + " reason: " + e.getMessage());
    }
    return null;
  }
}