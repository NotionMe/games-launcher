package ua.notion.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import ua.notion.utils.OsUtils;
import ua.notion.utils.Constants.Data;
import ua.notion.utils.OsUtils.OS;

public class ImageService {
    public static URI checkUriImage(String searchImage) {
        if (searchImage == null || searchImage.isBlank())
            return null;

        try {
            if (searchImage.startsWith("http:") || searchImage.startsWith("https:")) {
                String downloadedPath = downloadImage(searchImage);
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

    // Поки що путь захаркоджений
    private static String downloadImage(String imageUrlString)
            throws IOException, URISyntaxException {
        URL url = new URI(imageUrlString).toURL();
        String path = getRandomName();

        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
        }
        return path;
    }

    private static String getRandomName() {
        Random random = new Random();
        int value = random.nextInt(999);
        String path = null;

        switch (OsUtils.getCurrentOs()) {
            case OS.LINUX -> path = Data.HOME_PATH.getPath() + "/Downloads/" + value + ".png";
            case OS.WINDOWS -> path =
                    System.getProperty("user.home") + "\\Downloads\\" + value + ".png";

            default -> {
                break;
            }
        }
        return path;
    }
}
