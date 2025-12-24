package ua.notion.data.dto;

import java.util.List;

public record UserDTO(String id, List<GameDTO> library) {
}
