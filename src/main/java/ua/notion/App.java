package ua.notion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
        new Image("icon_16x16.png"),
        new Image("icon_32x32.png"),
        new Image("icon_64x64.png"),
        new Image("icon_128x128.png"),
        new Image("icon_256x256.png"),
        new Image("icon_1024x1024.png")
    );

    primaryStage.setTitle("Games launcher");

    primaryStage.setScene(scene);
    primaryStage.show();
  }
}