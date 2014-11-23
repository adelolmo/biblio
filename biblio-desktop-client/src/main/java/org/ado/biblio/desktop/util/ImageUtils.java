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

    public static File writeCover(InputStream inputStream, String filename) throws IOException {
        return write(inputStream, filename, ".jpeg");
    }

    public static File write(InputStream inputStream, String filename, String extension) throws IOException {
        final File file = new File(DIRECTORY, filename.concat(extension));
        FileUtils.touch(file);
        FileOutputStream out = new FileOutputStream(file);
        IOUtils.copy(inputStream, out);
        return file;
    }

    public static Image readCoverOrDefault(String filename) {
        return readOrDefault(filename, ".jpeg");
    }

    public static Image readOrDefault(String filename, String extension) {
        final InputStream inputStream = read(filename, extension);
        if (inputStream != null) {
            return new Image(inputStream);
        } else {
            return getDefaulImage();
        }
    }

    public static InputStream read(String filename, String extension) {
        try {
            return new FileInputStream(new File(DIRECTORY, filename.concat(extension)));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static Image getImageOrDefault(InputStream inputStream) {
        if (inputStream != null) {
            return new Image(inputStream);
        } else {
            return getDefaulImage();
        }
    }

    private static Image getDefaulImage() {
        return new Image(ImageUtils.class.getResourceAsStream("cover_not_found.png"));
    }
}
