package ua.notion.components;

public record Game(String title, String targetPath, String launchArguments, String pfx, String dllWineOverride,
        String defaultProtonVersion, String iconPath, String coverPath) {

}
