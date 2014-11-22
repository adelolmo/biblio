package org.ado.biblio.desktop.util;

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
public class ImageWriter {

    private static final File DIRECTORY = new File(AppConfiguration.APP_CONFIG_DIRECTORY, "covers");

    public static void write(InputStream inputStream, String filename, String extension) throws IOException {
        final File file = new File(DIRECTORY, filename.concat(extension));
        FileUtils.touch(file);
        FileOutputStream out = new FileOutputStream(file);
        IOUtils.copy(inputStream, out);
    }

    public static InputStream read(String filename, String extension) {
        try {
            return new FileInputStream(new File(DIRECTORY, filename.concat(extension)));
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
