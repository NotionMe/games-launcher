package ua.notion.controllers.settings;

import java.io.File;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ua.notion.components.User;
import ua.notion.data.UserData;
import ua.notion.utils.Constants.UI;

public class PathsPageController {

  private User user;

  private final UserData userData = new UserData();

  private static final String PATH_TO_YOUR_DIRECTORY = "Path/to/you/directory";

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

    updateLabel();

    rootPane.getStylesheets().addAll(
        getClass().getResource(UI.PATHS_PAGE_CSS).toExternalForm(),
        getClass().getResource(UI.BASE_CSS).toExternalForm()
    );
  }

  @FXML
  private void onChangeDownloadPath(MouseEvent event) {
    handlePathChange("Select Download Folder", user.getPaths().getPathForGameDownload(), downloadPathLabel,
        user.getPaths()::setPathForGameDownload);
  }

  @FXML
  private void onChangeInstallPath(MouseEvent event) {
    handlePathChange("Select Install Folder", user.getPaths().getPathForGameInstalls(), installPathLabel,
        user.getPaths()::setPathForGameInstalls);
  }

  @FXML
  private void onChangeProtonPath(MouseEvent event) {
    handlePathChange("Select Proton Folder", user.getPaths().getPathForProtonInstalls(), protonPathLabel,
        user.getPaths()::setPathForProtonInstalls);
  }

  @FXML
  private void onChangePrefixPath(MouseEvent event) {
    handlePathChange("Select Prefix Folder", user.getPaths().getPathForPrefixes(), prefixPathLabel,
        user.getPaths()::setPathForPrefixes);
  }

  private void handlePathChange(String title, String currentPath, Label label,
      Consumer<String> setter) {
    File selectedDirectory = chooseDirectory(title, currentPath);
    if (selectedDirectory != null) {
      String newPath = selectedDirectory.getAbsolutePath();
      label.setText(newPath);
      setter.accept(newPath);
      userData.write(user);
    }
  }

  private File chooseDirectory(String title, String currentPath) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle(title);

    if (currentPath != null && !currentPath.isEmpty()) {
      File initialDir = new File(currentPath);
      if (initialDir.exists()) {
        directoryChooser.setInitialDirectory(initialDir);
      }
    }

    Stage stage = (Stage) rootPane.getScene().getWindow();
    return directoryChooser.showDialog(stage);
  }

  private void updateLabel() {
    user = userData.read();

    validateAndSetPath(downloadPathLabel, user.getPaths().getPathForGameDownload(), PATH_TO_YOUR_DIRECTORY);
    validateAndSetPath(installPathLabel, user.getPaths().getPathForGameInstalls(), "Choose every time");
    validateAndSetPath(protonPathLabel, user.getPaths().getPathForProtonInstalls(), PATH_TO_YOUR_DIRECTORY);
    validateAndSetPath(prefixPathLabel, user.getPaths().getPathForPrefixes(), PATH_TO_YOUR_DIRECTORY);
  }

  private void validateAndSetPath(Label label, String path, String defaultText) {
    if (path != null && !path.isEmpty() && new File(path).exists()) {
      label.setText(path);
    } else {
      label.setText(defaultText);
    }
  }
}