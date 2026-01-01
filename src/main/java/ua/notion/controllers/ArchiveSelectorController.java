package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArchiveSelectorController {

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox filesContainer;

    private CompletableFuture<String> resultFuture;

    @FXML
    private void initialize() {
        rootPane.setOnMouseClicked(event -> {
            if (event.getTarget() == rootPane) {
                cancel();
            }
        });
    }

    public void setFiles(List<File> files) {
        filesContainer.getChildren().clear();
        String workingDir = System.getProperty("user.dir");
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (path.startsWith(workingDir)) {
                path = path.substring(workingDir.length());
                if (path.startsWith(File.separator)) {
                    path = path.substring(1);
                }
            }
            Label fileLabel = new Label(path);
            fileLabel.setMaxWidth(Double.MAX_VALUE);
            fileLabel.getStyleClass().add("file-item");
            fileLabel.setTooltip(new Tooltip(file.getAbsolutePath()));
            fileLabel.setOnMouseClicked(e -> selectFile(file.getAbsolutePath()));
            filesContainer.getChildren().add(fileLabel);
        }
    }

    public void setResultFuture(CompletableFuture<String> future) {
        this.resultFuture = future;
    }

    private void selectFile(String file) {
        if (resultFuture != null) {
            resultFuture.complete(file);
        }
        closeWindow();
    }

    private void cancel() {
        if (resultFuture != null) {
            resultFuture.cancel(true);
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
