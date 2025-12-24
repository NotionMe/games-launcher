package ua.notion.services;

import java.util.List;


public class LauchingServise {

    public static void runCommand(List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
