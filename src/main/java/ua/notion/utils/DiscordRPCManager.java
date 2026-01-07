package ua.notion.utils;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DiscordRPCManager {

    private static final System.Logger LOGGER = System.getLogger(DiscordRPCManager.class.getName());

    private String stateArgs = "test State";
    private String detailArgs = "test details";
    private String isPlaying = "test is playing";
    private String urlGame = "http://google.com";

    private static Process pythonProcess;

    public void injectPythonRPC() {
        if (!checkEnvironmentFolder()) {
            try {
                createEnvironmentFolder();

            } catch (Exception e) {
                LOGGER.log(ERROR, "Error create venv: {0}", e.getMessage());
            }
        }

        if (pythonProcess != null && pythonProcess.isAlive()) {
            LOGGER.log(INFO, "Discord RPC is already running.");
            return;
        }

        String projectRoot = System.getProperty("user.dir");
        String pythonExe = projectRoot + "/venv/bin/python";
        String scriptPath = projectRoot + "/src/main/resources/script/python/discordRPC.py";

        List<String> execute =
                List.of(pythonExe, scriptPath, stateArgs, detailArgs, isPlaying, urlGame);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(execute);
            processBuilder.redirectErrorStream(true);

            pythonProcess = processBuilder.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (pythonProcess != null && pythonProcess.isAlive()) {
                    LOGGER.log(INFO, "Stopping Discord RPC...");
                    pythonProcess.destroy();
                }
            }));

            try (BufferedReader in =
                    new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    LOGGER.log(INFO, "Python output: {0}", line);
                }
            }

            int exitCode = pythonProcess.waitFor();
            LOGGER.log(INFO, "Python script exited with code: {0}", exitCode);

        } catch (Exception e) {
            LOGGER.log(ERROR, "Error starting Discord RPC: {0}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopRPC() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroy();
            LOGGER.log(INFO, "Discord RPC stopped manually.");
        }
    }

    private boolean checkEnvironmentFolder() {
        String projectRoot = System.getProperty("user.dir");
        String pythonVenv = projectRoot + "/venv";
        String pythonExe = pythonVenv + "/bin/python";

        File venv = new File(pythonVenv);
        File exe = new File(pythonExe);

        if (venv.isDirectory()) {
            if (exe.exists()) {
                return true;
            }
        }


        return false;
    }

    private void createEnvironmentFolder() throws IOException {
        String projectRoot = System.getProperty("user.dir");
        File workingDirectory = new File(projectRoot);

        try {
            LOGGER.log(INFO, "Creating virtual environment...");
            Process venvProcess = new ProcessBuilder("python3", "-m", "venv", "venv")
                    .directory(workingDirectory).inheritIO().start();

            int venvExitCode = venvProcess.waitFor();
            if (venvExitCode != 0) {
                throw new IOException("Failed to create venv, exit code: " + venvExitCode);
            }

            LOGGER.log(INFO, "Installing requirements...");
            String pipPath = projectRoot + "/venv/bin/pip";
            Process pipProcess = new ProcessBuilder(pipPath, "install", "-r", "requirements.txt")
                    .directory(workingDirectory).inheritIO().start();

            int pipExitCode = pipProcess.waitFor();
            if (pipExitCode != 0) {
                throw new IOException("Failed to install requirements, exit code: " + pipExitCode);
            }

            LOGGER.log(INFO, "Environment setup complete.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Environment setup was interrupted", e);
        }
    }
}
