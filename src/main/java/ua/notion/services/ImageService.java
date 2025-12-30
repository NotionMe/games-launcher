package ua.notion.services;

import java.io.File;
import java.net.URI;
import java.net.URL;
import javafx.scene.image.Image;
import ua.notion.utils.PackageInstaller;

public class ImageService {
    private final PackageInstaller installer = new PackageInstaller();

    public URI checkUriImage(String searchImage) {
        if (searchImage == null || searchImage.isBlank())
            return null;

        try {
            if (searchImage.startsWith("http:") || searchImage.startsWith("https:")) {
                String cachePath = installer.getCachePath(searchImage, ".png");
                File cacheFile = new File(cachePath);

                if (cacheFile.exists()) {
                    return cacheFile.toURI();
                }

                String downloadedPath = installer.downloadUrl(searchImage, cachePath);
                return new File(downloadedPath).toURI();
            }

            if (searchImage.startsWith("file:") || searchImage.startsWith("jar:")) {
                return URI.create(searchImage);
            }

            File file = new File(searchImage);
            if (file.exists()) {
                return file.toURI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Image loadImage(String path, String defaultResource, Class<?> context) {
        if (path != null && !path.isBlank()) {
            URI uri = checkUriImage(path);
            if (uri != null) {
                try {
                    return new Image(uri.toString(), true);
                } catch (Exception e) {
                    System.err.println("Failed to load image from URI: " + uri);
                }
            }
            try {
                URL resource = context.getResource(path);
                if (resource != null) {
                    return new Image(resource.toExternalForm());
                }
            } catch (Exception e) {
            }
        }
        if (defaultResource != null) {
            URL defaultUrl = context.getResource(defaultResource);
            if (defaultUrl != null) {
                return new Image(defaultUrl.toExternalForm());
            }
        }
        return null;
    }
}
