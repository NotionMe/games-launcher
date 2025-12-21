package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class GameCardController {

    @FXML
    private StackPane card;
    @FXML
    private ImageView cover;
    @FXML
    private ImageView icon;
    @FXML
    private Label title;
    
    public void setCover(String resourcePath) {
        cover.setImage(new Image(getClass().getResource(resourcePath).toExternalForm()));
    }

    public void setIcon(String resourcePath) {
        icon.setImage(new Image(getClass().getResource(resourcePath).toExternalForm()));
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    @FXML
    private void cardPressAction() {
        SideDrawerController.openDrawer();

    }

    @FXML
    private void initialize() {}
}
