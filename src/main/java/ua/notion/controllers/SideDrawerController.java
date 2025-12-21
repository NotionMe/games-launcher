package ua.notion.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class SideDrawerController {

    @FXML
    private Button playButton;
    @FXML
    private Label timeLable;
    @FXML
    private GridPane actionsGrid;
    @FXML
    private Button debugButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button utilButton;
    @FXML
    private Button runButton;
    @FXML
    private Button foldersButton;
    @FXML
    private Button steamdbButton;
    @FXML
    private Button protonButton;
    @FXML
    private Button steamButton;
    @FXML
    private ToggleButton desktopButton;
    @FXML
    private ToggleButton appMenuButton;
    @FXML
    private ToggleButton nonSteamButton;


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

    @FXML
    private void onPlayAction() {
        System.out.println("you press button Play");
    }

    @FXML
    private void onDebugAction() {
        System.out.println("you press button Debug");
    }

    @FXML
    private void onSettingsAction() {
        System.out.println("you press button Settings");
    }

    @FXML
    private void onRemoveAction() {
        System.out.println("you press button Remove");
    }

    @FXML
    private void onUtilAction() {
        System.out.println("you press button Utility");
    }

    @FXML
    private void onRunAction() {
        System.out.println("you press button Run");
    }

    @FXML
    private void onFoldersAction() {
        System.out.println("you press button Folders");
    }

    @FXML
    private void onSteamDbAction() {
        System.out.println("you press button SteamDB");
    }

    @FXML
    private void onProtonAction() {
        System.out.println("you press button Proton");
    }

    @FXML
    private void onSteamAction() {
        System.out.println("you press button Steam");
    }

    @FXML
    private void onDesktopAction() {
        System.out.println("you press button Desktop shortcut");
    }

    @FXML
    private void onAppMenuAction() {
        System.out.println("you press button App menu shortcut");
    }

    @FXML
    private void onNonSteamAction() {
        System.out.println("you press button Non-steam shortcut");
    }

    public AnchorPane getDrawerRoot() {
        return drawerRoot;
    }
}
