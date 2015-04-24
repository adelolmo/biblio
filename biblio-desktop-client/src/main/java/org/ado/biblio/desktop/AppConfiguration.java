package org.ado.biblio.desktop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

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
 * Class to access application properties and handle user's configuration options.
 * It also contains some application constants.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class AppConfiguration {

    public static final File APP_CONFIG_DIRECTORY = new File(FileUtils.getUserDirectory(), "biblio");
    public static final File DATABASE_FILE = new File(APP_CONFIG_DIRECTORY, "biblio.db");
    private static final File CONFIG = new File(APP_CONFIG_DIRECTORY, "biblio-config.properties");

    private static Properties config;
    private static Properties properties;

    public static String getApplicationProperty(String name) {
        if (properties == null) {
            try {
                properties = loadFileProperties(new File("biblio.properties"), false);
            } catch (Exception e) {
                // fallback
                System.out.println("loading fallback properties");
                properties = loadFileProperties(AppConfiguration.class.getResourceAsStream("biblio.properties"));
            }
        }
        return properties.getProperty(name);
    }

    public static void setConfigurationProperty(String property, String value) {
        init();
        config.put(property, value);
        store();
    }

    public static String getConfigurationProperty(String property) {
        init();
        return config.getProperty(property);
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
        config = loadFileProperties(CONFIG, true);
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

    private static Properties loadFileProperties(File file, boolean createIfNotExist) {
        Properties prop = new Properties();
        try {
            if (!file.exists()) {
                if (createIfNotExist) {
                    FileUtils.touch(file);
                } else {
                    throw new IOException(String.format("Properties file \"%s\" does not exits.", file.getAbsolutePath()));
                }
            }
            prop.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read application configuration file", e);
        }
        return prop;
    }
}
