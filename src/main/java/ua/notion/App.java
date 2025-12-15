package ua.notion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    BorderPane root = new FXMLLoader().load(getClass().getResource("main-menu.fxml")); // orig PARENT
    primaryStage.initStyle(StageStyle.TRANSPARENT);
    Scene scene = new Scene(root);
    scene.setFill(Color.TRANSPARENT);
    primaryStage.getIcons().addAll(
        new Image(getClass().getResource("/icons/icon_16x16.png").toString()),
        new Image(getClass().getResource("/icons/icon_32x32.png").toString()),
        new Image(getClass().getResource("/icons/icon_64x64.png").toString()),
        new Image(getClass().getResource("/icons/icon_128x128.png").toString()),
        new Image(getClass().getResource("/icons/icon_256x256.png").toString()),
        new Image(getClass().getResource("/icons/icon_1024x1024.png").toString())
    );
    primaryStage.setTitle("Games launcher");

    primaryStage.setScene(scene);
    primaryStage.show();

  }
}