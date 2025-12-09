package ua.notion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Games launcher");
    Button button = new Button();
    button.setText("Hi");

    button.setOnAction(event -> {
      System.out.println("Pressed!");
      button.setText("Pressed!");
    });

    StackPane layout = new StackPane();
    layout.getChildren().add(button);

    Scene scene = new Scene(layout, 640, 480);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}