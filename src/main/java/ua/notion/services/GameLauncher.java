package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ua.notion.components.Game;
import ua.notion.utils.Constants.Data;

public class GameLauncher {

    private GameLauncher() {}

    public static void launch(Game game) {
        String path = game.targetPath();
        File gameFile = new File(path);
        File workDir = gameFile.getParentFile();
        String script = Data.SCRIPT_PATH.getAbsolutePath();
        String protonPath = game.defaultProtonVersion();

        List<String> command = new ArrayList<>();
        command.add(script);
        command.add(protonPath);
        command.add(game.targetPath());
        command.add(game.pfx());
        command.add(game.dllWineOveride());
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workDir);
            pb.inheritIO();

            pb.start();
            System.out.println("Start game: " + game.title());

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
