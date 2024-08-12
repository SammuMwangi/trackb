
package org.traccar.handler;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.database.StatisticsManager;
import org.traccar.model.Position;
import org.traccar.storage.Storage;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Request;

public class DatabaseHandler extends BasePositionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHandler.class);

    private final Storage storage;
    private final StatisticsManager statisticsManager;

    @Inject
    public DatabaseHandler(Storage storage, StatisticsManager statisticsManager) {
        this.storage = storage;
        this.statisticsManager = statisticsManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {

        try {
            position.setId(storage.addObject(position, new Request(new Columns.Exclude("id"))));
            statisticsManager.registerMessageStored(position.getDeviceId(), position.getProtocol());
        } catch (Exception error) {
            LOGGER.warn("Failed to store position", error);
        }

        callback.processed(false);
    }

}
