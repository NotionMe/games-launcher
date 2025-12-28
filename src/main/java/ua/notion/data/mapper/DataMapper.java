package ua.notion.data.mapper;

import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.dto.GameDTO;
import ua.notion.data.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DataMapper {

  public static User toEntity(UserDTO dto) {
    User user = new User(dto.id());
    if (dto.paths() != null) {
      user.getPaths().setPathForGameDownload(dto.paths().getPathForGameDownload());
      user.getPaths().setPathForGameInstalls(dto.paths().getPathForGameInstalls());
      user.getPaths().setPathForProtonInstalls(dto.paths().getPathForProtonInstalls());
      user.getPaths().setPathForPrefixes(dto.paths().getPathForPrefixes());
    }
    if (dto.userLauncherSettings() != null) {
      user.getLauncherSettings().setFullScreen(dto.userLauncherSettings().isFullScreen());
      user.getLauncherSettings().setSteamDisabled(dto.userLauncherSettings().isSteamDisabled());
      user.getLauncherSettings()
         .setAnimationDisabled(dto.userLauncherSettings().isAnimationDisabled());
    }
    if (dto.library() != null) {
      dto.library()
          .forEach(g -> user.addGame(new Game(g.title(), g.path(), g.launchArguments(), g.pfx(),
              g.dllWineOverride(), g.defaultProtonVersion(), g.iconPath(), g.coverPath())));
    }
    return user;
  }

  public static UserDTO toDTO(User user) {
    List<GameDTO> gameDTOs = user.getLibrary().stream()
        .map(
            g -> new GameDTO(g.title(), g.targetPath(), g.launchArguments(), g.pfx(),
                g.dllWineOverride(),
                g.defaultProtonVersion(), g.iconPath(), g.coverPath()))
        .collect(Collectors.toList());
    return new UserDTO(user.getId(), user.getPaths(), user.getLauncherSettings(), gameDTOs);
  }
}
