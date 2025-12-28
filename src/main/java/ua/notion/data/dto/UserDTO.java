package ua.notion.data.dto;

import java.util.List;
import ua.notion.components.UserLauncher;
import ua.notion.components.UserPaths;

public record UserDTO(String id, UserPaths paths, UserLauncher launcherSettings, List<GameDTO> library) {
}
