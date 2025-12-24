package ua.notion.controllers.settings;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ua.notion.utils.Constants.UI;

public class PathsPageController {

  @FXML
  private StackPane rootPane;

  @FXML
  private Label downloadPathLabel;
  @FXML
  private Label installPathLabel;
  @FXML
  private Label protonPathLabel;
  @FXML
  private Label prefixPathLabel;

  public void initialize() {

      rootPane.getStylesheets().addAll(
          getClass().getResource(UI.PATHS_PAGE_CSS).toExternalForm(),
          getClass().getResource(UI.BASE_CSS).toExternalForm()
      );
    // something like:
    // downloadPathLabel.setText(config.getDownloadPath());
  }

  @FXML
  private void onChangeDownloadPath(MouseEvent event) {
    File selectedDirectory = chooseDirectory("Select Download Folder");
    if (selectedDirectory != null) {
      downloadPathLabel.setText(selectedDirectory.getAbsolutePath());
      // TODO: Save path in config
    }
  }

  @FXML
  private void onChangeInstallPath(MouseEvent event) {
    File selectedDirectory = chooseDirectory("Select Install Folder");
    if (selectedDirectory != null) {
      installPathLabel.setText(selectedDirectory.getAbsolutePath());
      // TODO: Saveeeee
    }
  }

  @FXML
  private void onChangeProtonPath(MouseEvent event) {
    File selectedDirectory = chooseDirectory("Select Proton Folder");
    if (selectedDirectory != null) {
      protonPathLabel.setText(selectedDirectory.getAbsolutePath());
      // TODO: SAVE AWP BRO
    }
  }

  @FXML
  private void onChangePrefixPath(MouseEvent event) {
    File selectedDirectory = chooseDirectory("Select Prefix Folder");
    if (selectedDirectory != null) {
      prefixPathLabel.setText(selectedDirectory.getAbsolutePath());
      // TODO: Save
    }
  }

  private File chooseDirectory(String title) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle(title);

    Stage stage = (Stage) rootPane.getScene().getWindow();
    return directoryChooser.showDialog(stage);
  }
}