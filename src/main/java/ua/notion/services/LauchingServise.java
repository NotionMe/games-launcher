package ua.notion.services;

import java.nio.file.Files;
import java.util.List;
import ua.notion.utils.Constants.Data;

public class LauchingServise {

    public static void createPrefixPath() {
        if (Data.PREFIX_PATH.isAbsolute())
            return;

        try {
            Files.createDirectory(Data.PREFIX_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runCommand(List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}