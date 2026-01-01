package ua.notion.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PackageInstaller {
    public String downloadUrl(String urlString, String targetPath)
            throws IOException, URISyntaxException {
        URL url = new URI(urlString).toURL();

        File targetFile = new File(targetPath);
        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (InputStream in = url.openStream()) {
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.err.println("Failed to download: " + urlString);
            throw e;
        }
        return targetPath;
    }

    public String getCachePath(String imageUrlString, String format) {
        String fileName = "cache_" + Integer.toHexString(imageUrlString.hashCode()) + format;

        String baseDir = System.getProperty("user.dir");
        //TODO: hard code path need fix! [cache/image] be like create in temp (linux) || appdata (windows)  
        File cacheDir = new File(baseDir, "cache/images");

        return new File(cacheDir, fileName).getAbsolutePath();
    }
}
