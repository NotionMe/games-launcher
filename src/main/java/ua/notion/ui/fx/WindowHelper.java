package ua.notion.ui.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import ua.notion.services.GameService;

public class WindowHelper {
    public static void navigateBack(StackPane centerLayer, AnchorPane bottomPanel) {
        if (!centerLayer.getChildren().isEmpty()) {
            centerLayer.getChildren().remove(centerLayer.getChildren().size() - 1);
            GameService.showPanelVisible(bottomPanel);
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
