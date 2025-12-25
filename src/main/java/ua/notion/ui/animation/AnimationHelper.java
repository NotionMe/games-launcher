package ua.notion.ui.animation;

import java.net.URI;
import java.net.URL;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ua.notion.services.ImageService;

public class AnimationHelper {
    public void transitionToBackground(String imagePath, ImageView imageView) {
        try {
            if (imagePath == null || imagePath.isBlank()) {
                restoreDefaultBackground(imageView);
                return;
            }

            Image image = ImageService.loadImage(imagePath, null, getClass());

            if (image != null) {
                imageView.setImage(image);

                FadeTransition ft = new FadeTransition(Duration.millis(200), imageView);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
            } else {
                restoreDefaultBackground(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restoreDefaultBackground(ImageView imageView) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), imageView);
        ft.setFromValue(imageView.getOpacity());
        ft.setToValue(0.0);
        ft.play();
    }

}
