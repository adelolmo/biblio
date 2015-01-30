package org.ado.biblio.desktop.install;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author sMeet, 30.01.15
 */
public class InstallPresenter {

    @FXML
    private Label labelStepOne;

    @FXML
    private Label labelStepTwo;

    @FXML
    private Label labelStepThree;
    private Stage _stage;


    public void process(Stage stage) {
        _stage = stage;
        try {
            final String updateFilePath = "update.zip";
            final File updateFile = new File(updateFilePath);
            if (updateFile.exists()) {
                System.out.println("installing update...");
                labelStepOne.setText("1. Application update found");
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

    private void extractFolder(String zipFile) throws IOException {
        labelStepTwo.setText("2. Extracting update file");

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
        labelStepThree.setText("3. Installation process successfully finished.");
    }
}