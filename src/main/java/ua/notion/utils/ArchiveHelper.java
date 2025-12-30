package ua.notion.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ArchiveHelper {
    private static final Logger LOGGER = System.getLogger(ArchiveHelper.class.getName());

    public static Path extract(String archivePath, String outputDirPath) throws IOException {
        Path archiveFile = Path.of(archivePath);
        Path outputDir = Path.of(outputDirPath);
        Path topLevelDir = null;

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

                if (topLevelDir == null) {
                    Path relativeEntryPath = Paths.get(entry.getName());
                    if (relativeEntryPath.getNameCount() >= 1) {
                        topLevelDir = outputDir.resolve(relativeEntryPath.getName(0));
                    }
                }

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
            return topLevelDir != null ? topLevelDir : outputDir;
        } catch (CompressorException e) {
            throw new IOException("Failed to create compressor stream", e);
        }
    }

    public static void deleteArchive(String path) {
        Path pathToArchive = Paths.get(path);
        try {
            if (Files.deleteIfExists(pathToArchive)) {
                LOGGER.log(Level.INFO, "Archive deleted: {0}", path);
            }
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Failed to delete archive: {0}", path, e);
        }
    }

    private static InputStream createCompressorInputStream(InputStream in)
            throws CompressorException {
        return new CompressorStreamFactory().createCompressorInputStream(in);
    }
}

