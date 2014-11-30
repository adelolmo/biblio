package org.ado.biblio.desktop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class AppConfiguration {

    public static final File APP_CONFIG_DIRECTORY = new File(FileUtils.getUserDirectory(), "biblio");
    private static final File CONFIG = new File(APP_CONFIG_DIRECTORY, "biblio-config.properties");

    private static Properties config;
    private static Properties properties;

    public static String getProperty(String name) {
        if (properties == null) {
            properties = loadFileProperties(AppConfiguration.class.getResourceAsStream("biblio.properties"));
        }
        return properties.getProperty(name);
    }

    public static String getAppId() {
        init();
        String id = config.getProperty("id");
        if (StringUtils.isBlank(id)) {
            id = UUID.randomUUID().toString();
            config.put("id", id);
            store();
        }
        return id;
    }

    private static void store() {
        try {
            config.store(FileUtils.openOutputStream(CONFIG), "Biblio Configuration");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot save application configuration file", e);
        }
    }

    private static void init() {
        config = loadFileProperties(CONFIG);
        store();
    }

    private static Properties loadFileProperties(InputStream inputStream) {
        final Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read application properties file", e);
        }
        return prop;
    }

    private static Properties loadFileProperties(File file) {
        Properties prop = new Properties();
        try {
            if (!file.exists()) {
                FileUtils.touch(file);
            }
            prop.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read application configuration file", e);
        }
        return prop;
    }
}
