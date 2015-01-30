package org.ado.biblio.desktop;

import org.ado.biblio.desktop.util.ZipArchiver;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @author sMeet, 30.01.15
 */
public class InstallUpdate {

    public static void main(String[] args) {
        final ZipArchiver zipArchiver = new ZipArchiver();
        try {
//            zipArchiver.unpackZip(new File("update.zip"), new File("/tmp/update"));
            final String updateFilePath = "update.zip";
            final File updateFile = new File(updateFilePath);
            if (updateFile.exists()) {
                System.out.println("installing update...");
                extractFolder(updateFilePath);
                FileUtils.deleteQuietly(updateFile);
            } else {
                System.out.println("no update found");
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    static public void extractFolder(String zipFile) throws ZipException, IOException {
        System.out.println(zipFile);
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
//        String newPath = zipFile.substring(0, zipFile.length() - 4);
        String newPath = System.getProperty("user.dir");
        System.out.println(newPath);

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            //destFile = new File(newPath, destFile.getName());
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(zip
                        .getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[BUFFER];

                // write the current file to disk
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                        BUFFER);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }

            if (currentEntry.endsWith(".zip")) {
                // found a zip file, try to open
                extractFolder(destFile.getAbsolutePath());
            }
        }
    }
}