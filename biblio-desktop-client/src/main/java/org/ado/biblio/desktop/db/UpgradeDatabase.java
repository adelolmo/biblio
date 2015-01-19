package org.ado.biblio.desktop.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * @author Andoni del Olmo,
 * @since 19.01.15
 */
public class UpgradeDatabase {

    private final Logger LOGGER = LoggerFactory.getLogger(UpgradeDatabase.class);

    private Connection connection;

    public UpgradeDatabase(Connection connection) {
        this.connection = connection;
    }

    public void upgradeToVersion(int version) {
        LOGGER.info("upgrade DB to version {}", version);
        switch (version) {
            case 2:
                upgradeToVersionTwo();
        }
    }

    private void upgradeToVersionTwo() {

    }
}