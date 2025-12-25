package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javafx.scene.image.Image;

public class ImageService {
    public static URI checkUriImage(String searchImage) {
        if (searchImage == null || searchImage.isBlank())
            return null;

        try {
            if (searchImage.startsWith("http:") || searchImage.startsWith("https:")) {
                String cachePath = getCachePath(searchImage);
                File cacheFile = new File(cachePath);

                if (cacheFile.exists()) {
                    return cacheFile.toURI();
                }

                String downloadedPath = downloadImage(searchImage, cachePath);
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

    private static String downloadImage(String imageUrlString, String targetPath)
            throws IOException, URISyntaxException {
        URL url = new URI(imageUrlString).toURL();

        File targetFile = new File(targetPath);
        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (InputStream in = url.openStream()) {
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("Failed to download image: " + imageUrlString);
            throw e;
        }
        return targetPath;
    }

    private static String getCachePath(String imageUrlString) {
        String fileName = "cache_" + Integer.toHexString(imageUrlString.hashCode()) + ".png";

        String baseDir = System.getProperty("user.dir");
        File cacheDir = new File(baseDir, "cache/images");

        return new File(cacheDir, fileName).getAbsolutePath();
    }

    public static Image loadImage(String path, String defaultResource, Class<?> context) {
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
