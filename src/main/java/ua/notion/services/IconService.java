package ua.notion.services;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;


public class IconService {

  private static final String ICONS_DIR = "images";

  public IconService() {
    createIconsDirectory();
  }

  private void createIconsDirectory() {
    try {
      Files.createDirectories(Paths.get(ICONS_DIR));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String extractAndSaveIcon(File file) {

    if (file == null || !file.exists()) {
      return null;
    }

    try {

      Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);

      BufferedImage bufferedImage = new BufferedImage(
          icon.getIconWidth(),
          icon.getIconHeight(),
          BufferedImage.TYPE_INT_ARGB
      );

      Graphics2D graphics2D = bufferedImage.createGraphics();
      icon.paintIcon(null, graphics2D, 0, 0);
      graphics2D.dispose();

      String iconName = file.getName() + ".png";
      Path destinationPath = Paths.get(ICONS_DIR, iconName);

      ImageIO.write(bufferedImage, "png", destinationPath.toFile());

      return "images/" + file.getName() + ".png";
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
