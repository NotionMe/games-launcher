package ua.notion.controllers.settings;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProtonItemController {

    @FXML
    private Button actionButton;

    @FXML
    private ImageView actionIcon;

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
}
