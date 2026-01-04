package ua.notion.ui.animation;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ua.notion.services.ImageService;

public class AnimationHelper {

  private final ImageService imageService = new ImageService();

  public void transitionToBackground(String imagePath, ImageView imageView,
      boolean animationsDisabled) {
    try {
      if (imagePath == null || imagePath.isBlank()) {
        restoreDefaultBackground(imageView, animationsDisabled);
        return;
      }

      Image image = imageService.loadImage(imagePath, null, getClass());

      if (image != null) {
        imageView.setImage(image);

        if (animationsDisabled) {
          imageView.setOpacity(1.0);
        } else {
          FadeTransition ft = new FadeTransition(Duration.millis(200), imageView);

          ft.setFromValue(0.0);
          ft.setToValue(1.0);
          ft.play();
        }
      } else {
        restoreDefaultBackground(imageView, animationsDisabled);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void restoreDefaultBackground(ImageView imageView, boolean animationsDisabled) {

    if (animationsDisabled) {
      imageView.setOpacity(0.0);
    } else {
      FadeTransition ft = new FadeTransition(Duration.millis(300), imageView);

      ft.setFromValue(imageView.getOpacity());
      ft.setToValue(0.0);
      ft.play();
    }
  }

}
