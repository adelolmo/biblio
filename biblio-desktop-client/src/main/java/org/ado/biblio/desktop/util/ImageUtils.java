package org.ado.biblio.desktop.util;

import javafx.scene.image.Image;
import org.ado.biblio.desktop.AppConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class ImageUtils {

    private static final File DIRECTORY = new File(AppConfiguration.APP_CONFIG_DIRECTORY, "covers");
    private static final String JPEG_EXTENSION = ".jpeg";
    private static final String COVER_NOT_FOUND_PNG = "cover_not_found.png";

    public static File writeCover(InputStream inputStream, String filename) throws IOException {
        return write(inputStream, filename, JPEG_EXTENSION);
    }

    public static File write(InputStream inputStream, String filename, String extension) throws IOException {
        final File file = new File(DIRECTORY, filename.concat(extension));
        FileUtils.touch(file);
        FileOutputStream out = new FileOutputStream(file);
        IOUtils.copy(inputStream, out);
        return file;
    }

    public static Image readCoverOrDefault(String filename) {
        return readOrDefault(filename, JPEG_EXTENSION);
    }

    public static Image readOrDefault(String filename, String extension) {
        final InputStream inputStream = read(filename, extension);
        if (inputStream != null) {
            return new Image(inputStream);
        } else {
            return getDefaultImage();
        }
    }

    public static InputStream read(String filename, String extension) {
        try {
            return new FileInputStream(new File(DIRECTORY, filename.concat(extension)));
        } catch (Exception e) {
            return null;
        }
    }

    public static Image getImageOrDefault(InputStream inputStream) {
        if (inputStream != null) {
            return new Image(inputStream);
        } else {
            return getDefaultImage();
        }
    }

    public static void deleteCover(String filename) {
        delete(filename, JPEG_EXTENSION);
    }

    private static void delete(String filename, String extension) {
        FileUtils.deleteQuietly(new File(DIRECTORY, filename.concat(extension)));
    }

    private static Image getDefaultImage() {
        return new Image(ImageUtils.class.getResourceAsStream(COVER_NOT_FOUND_PNG));
    }
}
