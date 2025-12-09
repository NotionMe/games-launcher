module ua.notion {
    requires javafx.controls;
    requires javafx.fxml;

    opens ua.notion to javafx.fxml;
    exports ua.notion;
}
