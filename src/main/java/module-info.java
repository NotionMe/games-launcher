module ua.notion {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.rmi;
  requires com.google.gson;
  requires javafx.graphics;
  requires java.desktop;

  opens ua.notion to javafx.fxml;

  opens ua.notion.controller to javafx.fxml;

  opens ua.notion.components to com.google.gson;

  exports ua.notion;

}
