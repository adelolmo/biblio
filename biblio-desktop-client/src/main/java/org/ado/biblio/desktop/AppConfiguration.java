package org.ado.biblio.desktop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class AppConfiguration {

    private static final File CONFIG = new File(FileUtils.getUserDirectoryPath(), "biblio-config.properties");

    private static Properties properties;

    private static void init() {
        properties = new Properties();
        try {
            if (!CONFIG.exists()) {
                FileUtils.touch(CONFIG);
            }
            properties.load(new FileInputStream(CONFIG));
            store();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read application configuration file", e);
        }
    }

    public static String getAppId() {
        init();
        String id = properties.getProperty("id");
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
            properties.put("id", id);
            store();
        }
        return id;
    }

    private static void store() {
        try {
            properties.store(FileUtils.openOutputStream(CONFIG), "Biblio Configuration");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot save application configuration file", e);
        }
    }
}
