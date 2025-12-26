package ua.notion.components;

import java.io.File;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.Constants.UI;

public record Game(String title, String targetPath, String pfx, String dllWineOveride,
        String defaultProtonVersion, String iconPath, String coverPath) {

}
