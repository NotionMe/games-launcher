package ua.notion.data.dto;

import java.util.List;
import ua.notion.components.UserPaths;

public record UserDTO(String id, UserPaths paths, List<GameDTO> library) {
}
