package ua.notion.services;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ua.notion.components.Game;
import ua.notion.components.User;
import ua.notion.data.UserData;
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
