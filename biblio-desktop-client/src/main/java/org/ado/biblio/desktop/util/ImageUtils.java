package org.ado.biblio.desktop.util;

import javafx.scene.image.Image;
import org.ado.biblio.desktop.AppConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Andoni del Olmo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
        notNull(inputStream, "inputStream cannot be null");
        notBlank(filename, "filename cannot be empty");

        return write(inputStream, filename, JPEG_EXTENSION);
    }

    public static File write(InputStream inputStream, String filename, String extension) throws IOException {
        notNull(inputStream, "inputStream cannot be null");
        notBlank(filename, "filename cannot be empty");
        notBlank(extension, "extension cannot be empty");

        final File file = new File(DIRECTORY, filename.concat(extension));
        FileUtils.touch(file);
        FileOutputStream out = new FileOutputStream(file);
        IOUtils.copy(inputStream, out);
        return file;
    }

    public static Image readCoverOrDefault(String filename) {
        notBlank(filename, "filename cannot be empty");

        return readOrDefault(filename, JPEG_EXTENSION);
    }

    public static Image readOrDefault(String filename, String extension) {
        notBlank(filename, "filename cannot be empty");
        notBlank(extension, "extension cannot be empty");

        final InputStream inputStream = read(filename, extension);
        if (inputStream != null) {
            return new Image(inputStream);
        } else {
            return getDefaultImage();
        }
    }

    public static InputStream read(String filename, String extension) {
        notBlank(filename, "filename cannot be empty");
        notBlank(extension, "extension cannot be empty");

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
        notBlank(filename, "filename cannot be empty");

        delete(filename, JPEG_EXTENSION);
    }

    private static void delete(String filename, String extension) {
        notBlank(filename, "filename cannot be empty");

        FileUtils.deleteQuietly(new File(DIRECTORY, filename.concat(extension)));
    }

    private static Image getDefaultImage() {
        return new Image(ImageUtils.class.getResourceAsStream(COVER_NOT_FOUND_PNG));
    }
}
