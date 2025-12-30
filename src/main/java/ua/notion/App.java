package ua.notion;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.data.UserRepository;
import ua.notion.utils.Constants.UI;
import ua.notion.utils.Constants.Views;
import ua.notion.utils.GEProtonAPI;
import ua.notion.utils.StageUtils;

public class App extends Application {

  private final UserRepository userRepository = new UserData();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    loadFonts();

    StackPane root = FXMLLoader.load(getClass().getResource(Views.MAIN_MENU));

    primaryStage.initStyle(StageStyle.TRANSPARENT);

    Scene scene = new Scene(root);
    scene.setFill(Color.TRANSPARENT);

    primaryStage.getIcons().addAll(
        new Image(getClass().getResource("/icons/logo/icon_logo_16x16.png").toString()),
        new Image(getClass().getResource("/icons/logo/icon_logo_32x32.png").toString()),
        new Image(getClass().getResource("/icons/logo/icon_logo_64x64.png").toString()),
        new Image(getClass().getResource("/icons/logo/icon_logo_128x128.png").toString()),
        new Image(getClass().getResource("/icons/logo/icon_logo_256x256.png").toString()),
        new Image(getClass().getResource("/icons/logo/icon_logo_1024x1024.png").toString()));

    primaryStage.setTitle("Games launcher");

    primaryStage.setScene(scene);
    StageUtils.configureScreenSize(primaryStage);

    User user = userRepository.findAll();

    primaryStage.setFullScreenExitHint("");

    if(user.getLauncherSettings().isFullScreen()){
      primaryStage.setFullScreen(true);
    }

    primaryStage.show();

    GEProtonAPI protonAPI = new GEProtonAPI();
    CompletableFuture.runAsync(() -> {
      protonAPI.getProtonVersion();
    });
  }

  private void loadFonts() {

    InputStream fontStream = getClass().getResourceAsStream(UI.FONT_MAIN_PATH);
    if (fontStream != null) {
      Font.loadFont(fontStream, UI.FONT_DEFAULT_LOAD_SIZE);
    } else {
      System.err.println("Cannot find FiraSans-Medium.ttf");
    }
  }
}