module ua.notion {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.rmi;
  requires com.google.gson;
  requires javafx.graphics;
  requires java.desktop;
  requires javafx.base;
  requires java.logging;
  requires org.controlsfx.controls;

  opens ua.notion to javafx.fxml;

  opens ua.notion.controllers to javafx.fxml;

  opens ua.notion.components to com.google.gson;

  exports ua.notion;
  opens ua.notion.controllers.settings to javafx.fxml;

}
