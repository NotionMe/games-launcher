package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ua.notion.components.Game;
import ua.notion.utils.Constants.Data;

public class GameLauncher {

    public void launch(Game game) {
        String path = game.targetPath();
        File gameFile = new File(path);
        File workDir = gameFile.getParentFile();
        String script = Data.SCRIPT_PATH.toString();

        List<String> command = new ArrayList<>();

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
