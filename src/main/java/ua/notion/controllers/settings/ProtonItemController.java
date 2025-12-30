package ua.notion.controllers.settings;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressIndicator;


public class ProtonItemController {

    @FXML
    private Button actionButton;

    @FXML
    private ImageView actionIcon;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private Label protonNameLabel;


    @FXML
    private Label extensionLabel;

    public void setData(String name, String extension, boolean isInstalled, Runnable onAction) {
        protonNameLabel.setText(name);
        extensionLabel.setText(extension);

        String iconPath = isInstalled ? "/icons/icon_trash_36x36.png"
                : "/icons/settings/icon_download_36x36.png";
        actionIcon.setImage(new Image(getClass().getResourceAsStream(iconPath)));

        actionButton.getStyleClass().removeAll("download-button", "delete-button");
        if (isInstalled) {
            actionButton.getStyleClass().add("delete-button");
        } else {
            actionButton.getStyleClass().add("download-button");
        }

        actionButton.setOnAction(event -> {
            if (onAction != null) {
                onAction.run();
            }
        });
    }

    public void setLoading(boolean loading) {
        actionButton.setVisible(!loading);
        actionButton.setManaged(!loading);
        loadingIndicator.setVisible(loading);
        loadingIndicator.setManaged(loading);
    }
}

