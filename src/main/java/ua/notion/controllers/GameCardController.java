package ua.notion.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import ua.notion.components.Game;
import ua.notion.services.ImageService;
import ua.notion.utils.Constants.UI;
import java.net.URI;
import java.net.URL;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

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
    private Game game;

    public void setGame(Game game) {
        this.game = game;
        setTitle(game.title());
        setCover(game.coverPath());
        setIcon(game.iconPath());
    }

    public void setCover(String resourcePath) {
        cover.setImage(loadImage(resourcePath, UI.DEFAULT_COVER_PATH));
    }

    public void setIcon(String resourcePath) {
        icon.setImage(loadImage(resourcePath, UI.DEFAULT_ICON_PATH));
    }

    private Image loadImage(String path, String defaultResource) {
        if (path != null && !path.isBlank()) {
            URI uri = ImageService.checkUriImage(path);
            if (uri != null) {
                try {
                    return new Image(uri.toString(), true);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to load image from path: {0}", path);
                }
            }
            try {
                URL resource = getClass().getResource(path);
                if (resource != null) {
                    return new Image(resource.toExternalForm());
                }
            } catch (Exception e) {
            }
        }
        URL defaultUrl = getClass().getResource(defaultResource);
        if (defaultUrl != null) {
            return new Image(defaultUrl.toExternalForm());
        }
        return null;
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

