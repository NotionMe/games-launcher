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
  requires java.net.http;

  opens ua.notion to javafx.fxml;

  opens ua.notion.controllers to javafx.fxml;

  opens ua.notion.components to com.google.gson;
  opens ua.notion.data.dto to com.google.gson;
  
  opens ua.notion.controllers.settings to javafx.fxml;

  exports ua.notion;
  exports ua.notion.components;
  exports ua.notion.data.dto;

}
