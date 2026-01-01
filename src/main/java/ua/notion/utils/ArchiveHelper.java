package ua.notion.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import ua.notion.utils.Constants.Data;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ArchiveHelper {
    private static final Logger LOGGER = System.getLogger(ArchiveHelper.class.getName());

    public List<File> extractTarArchive(String archivePath, String outputDirPath)
            throws IOException {
        Path archiveFile = Path.of(archivePath);
        Path outputDir = Path.of(outputDirPath);
        List<File> extractedFiles = new ArrayList<>();

        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        try (InputStream fi = Files.newInputStream(archiveFile);
                InputStream bi = new BufferedInputStream(fi);
                InputStream ci = createCompressorInputStream(bi);
                TarArchiveInputStream ti = new TarArchiveInputStream(ci)) {

            TarArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null) {
                if (!ti.canReadEntryData(entry)) {
                    LOGGER.log(Level.WARNING, "Could not read entry: {0}", entry.getName());
                    continue;
                }

                Path entryPath = outputDir.resolve(entry.getName()).normalize();

                if (!entryPath.startsWith(outputDir)) {
                    throw new IOException(
                            "Entry is outside of the target directory: " + entry.getName());
                }

                extractedFiles.add(entryPath.toFile());

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else if (entry.isSymbolicLink()) {
                    try {
                        Path linkTarget = Path.of(entry.getLinkName());
                        Files.deleteIfExists(entryPath);
                        Files.createSymbolicLink(entryPath, linkTarget);
                    } catch (IOException e) {
                        LOGGER.log(Level.WARNING, "Could not create symbolic link: {0} -> {1}",
                                entry.getName(), entry.getLinkName());
                    }
                } else {
                    Path parent = entryPath.getParent();
                    if (parent != null && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                    Files.copy(ti, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            LOGGER.log(Level.INFO, "Extraction complete: {0}", archivePath);
            return extractedFiles;
        } catch (CompressorException e) {
            throw new IOException("Failed to create compressor stream", e);
        }
    }

    public List<File> extractSmartArchive(String fileZip, File destDir) throws IOException {
        LOGGER.log(Level.INFO, "Starting smart extraction for: {0}", fileZip);
        File archiveFile = new File(fileZip);
        List<File> extractedFiles = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(archiveFile)) {
            Set<String> rootElements = new HashSet<>();
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                String rootName = name.contains("/") ? name.substring(0, name.indexOf("/")) : name;
                if (!rootName.isEmpty()) {
                    rootElements.add(rootName);
                }
            }

            File finalDestDir = destDir;
            if (rootElements.size() > 1 || (rootElements.size() == 1
                    && !isRootAFolder(zipFile, rootElements.iterator().next()))) {
                String archiveNameWithoutExt = archiveFile.getName().replaceFirst("[.][^.]+$", "");
                finalDestDir = new File(destDir, archiveNameWithoutExt);
                LOGGER.log(Level.INFO, "Smart extraction: extracting to subdirectory: {0}",
                        finalDestDir);
            }

            entries = zipFile.entries();
            byte[] buffer = new byte[4096];

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File newFile = new File(finalDestDir, entry.getName());
                extractedFiles.add(newFile);

                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    newFile.getParentFile().mkdirs();
                    try (InputStream is = zipFile.getInputStream(entry);
                            FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
            LOGGER.log(Level.INFO, "Smart extraction complete for: {0}", fileZip);
        }
        return extractedFiles;
    }

    private boolean isRootAFolder(ZipFile zipFile, String rootName) {
        ZipEntry entry = zipFile.getEntry(rootName + "/");
        return entry != null && entry.isDirectory();
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public void deleteArchive(String path) {
        Path pathToArchive = Paths.get(path);
        try {
            if (Files.deleteIfExists(pathToArchive)) {
                LOGGER.log(Level.INFO, "Archive deleted: {0}", path);
            }
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Failed to delete archive: {0}", path, e);
        }
    }

    public static String detectedArchive(String path) {
        String fileName = path.toLowerCase();

        return Data.ARCHIVE_EXTENSIONS.stream().filter(fileName::endsWith).findFirst()
                .map(ext -> ext.equals(".7zip") ? ".7z" : ext).orElse(null);
    }

    private static InputStream createCompressorInputStream(InputStream in)
            throws CompressorException {
        return new CompressorStreamFactory().createCompressorInputStream(in);
    }
}

