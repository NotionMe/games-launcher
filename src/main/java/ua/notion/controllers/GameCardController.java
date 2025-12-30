package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import ua.notion.components.Game;
import ua.notion.services.ImageService;
import ua.notion.utils.Constants.UI;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.INFO;

public class GameCardController {

    @FXML
    private StackPane card;
    @FXML
    private ImageView cover;
    @FXML
    private ImageView icon;
    @FXML
    private Label title;

    private static final Logger LOGGER = System.getLogger(GameCardController.class.getName());
    private final ImageService imageService = new ImageService();
    private Game game;

    public void setGame(Game game) {
        this.game = game;
        setTitle(game.title());
        setCover(game.coverPath());
        setIcon(game.iconPath());
    }

    private void setCover(String resourcePath) {
        cover.setImage(loadImage(resourcePath, UI.DEFAULT_COVER_PATH));
    }

    private void setIcon(String resourcePath) {
        icon.setImage(loadImage(resourcePath, UI.DEFAULT_ICON_PATH));
    }

    private Image loadImage(String path, String defaultResource) {
        return imageService.loadImage(path, defaultResource, getClass());
    }

    private void setTitle(String text) {
        title.setText(text);
    }

    @FXML
    private void cardPressAction() {
        if (game != null) {
            String path = game.targetPath();
            LOGGER.log(INFO, "choice game: " + game.title());
            LOGGER.log(INFO, "path to game: " + path);
        }
        SideDrawerController.show(game);
    }

    @FXML
    private void initialize() {}
}

