package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import ua.notion.components.Game;

public class GameCardController {

    @FXML
    private StackPane card;
    @FXML
    private ImageView cover;
    @FXML
    private ImageView icon;
    @FXML
    private Label title;

    private Game game;

    public void setGame(Game game) {
        this.game = game;
        setTitle(game.title());
        setCover(game.coverPath());
        setIcon(game.iconPath());
    }

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
        if (game != null) {
            String path = game.targetPath();
            System.out.println("Вибрано гру: " + game.title());
            System.out.println("Шлях до гри: " + path);
        }
        SideDrawerController.openDrawer();
    }

    @FXML
    private void initialize() {}
}

