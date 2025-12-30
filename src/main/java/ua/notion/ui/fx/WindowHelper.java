package ua.notion.ui.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.services.GameService;
import ua.notion.services.IconService;

public class WindowHelper {

    private final UserRepository userRepository = new UserData();
    private final IconService iconService = new IconService();
    private final GameService gameService = new GameService(userRepository, iconService);

    public void navigateBack(StackPane centerLayer, AnchorPane bottomPanel) {
        if (!centerLayer.getChildren().isEmpty()) {
            centerLayer.getChildren().remove(centerLayer.getChildren().size() - 1);
            gameService.showPanelVisible(bottomPanel);
        }
    }

    public Node navigateAdd(String pathToFxml) {
        Node node = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pathToFxml));
            node = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }
}
