package ua.notion.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SideDrawerController {

    private static SideDrawerController sDrawerController;
    private static MainMenuController mainMenuController;

    @FXML
    private AnchorPane drawerRoot;
    @FXML
    private Pane drawerScrim;
    @FXML
    private AnchorPane sideDrawer;

    @FXML
    private void initialize() {
        sDrawerController = this;
        drawerRoot.getStylesheets()
                .addAll(getClass().getResource("/css/side-drawer.css").toExternalForm());
    }

    public void setMainMenuController(MainMenuController mainMenuController) {
        SideDrawerController.mainMenuController = mainMenuController;
    }

    @FXML
    private void scrimPressAction() {
        closeDrawer();
    }


    public static void openDrawer() {
        if (sDrawerController == null)
            return;

        sDrawerController.drawerRoot.setVisible(true);
        sDrawerController.drawerRoot.setManaged(true);

        // hardcode image
        mainMenuController.transitionToBackground("/icons/test_Image_background.jpg");

        TranslateTransition tt =
                new TranslateTransition(Duration.millis(150), sDrawerController.sideDrawer);
        tt.setFromX(sDrawerController.sideDrawer.getPrefWidth());
        tt.setToX(0);
        tt.play();
    }

    public static void closeDrawer() {
        if (sDrawerController == null)
            return;

        mainMenuController.restoreDefaultBackground();

        TranslateTransition tt =
                new TranslateTransition(Duration.millis(150), sDrawerController.sideDrawer);
        tt.setFromX(0);
        tt.setToX(sDrawerController.sideDrawer.getPrefWidth());
        tt.setOnFinished(event -> {
            sDrawerController.drawerRoot.setVisible(false);
            sDrawerController.drawerRoot.setManaged(false);
        });
        tt.play();
    }


    public AnchorPane getDrawerRoot() {
        return drawerRoot;
    }
}
