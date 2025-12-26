package ua.notion.data.dto;

public record GameDTO(String title, String path, String launchArguments, String pfx, String dllWineOverride,
        String defaultProtonVersion, String iconPath, String coverPath) {
}

