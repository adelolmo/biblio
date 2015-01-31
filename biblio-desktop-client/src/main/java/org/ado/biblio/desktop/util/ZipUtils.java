package org.ado.biblio.desktop.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Andoni del Olmo,
 * @since 31.01.15
 */
public class ZipUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);
    private static final int BUFFER_SIZE = 2048;

    public static void extractFolder(File zipFile, File destDirectory) throws IOException {
        LOGGER.info(zipFile.getAbsolutePath());

        ZipFile zip = new ZipFile(zipFile);
        destDirectory.mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(destDirectory, currentEntry);
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[BUFFER_SIZE];

                // write the current file to disk
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, BUFFER_SIZE)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }

            if (currentEntry.endsWith(".zip")) {
                // found a zip file, try to open
                extractFolder(zipFile, destDirectory);
            }
        }
    }
}