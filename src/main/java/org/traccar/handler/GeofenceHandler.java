
package org.traccar.handler;

import jakarta.inject.Inject;
import org.traccar.config.Config;
import org.traccar.helper.model.GeofenceUtil;
import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;

import java.util.List;

public class GeofenceHandler extends BasePositionHandler {

    private final Config config;
    private final CacheManager cacheManager;

    @Inject
    public GeofenceHandler(Config config, CacheManager cacheManager) {
        this.config = config;
        this.cacheManager = cacheManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {

        List<Long> geofenceIds = GeofenceUtil.getCurrentGeofences(config, cacheManager, position);
        if (!geofenceIds.isEmpty()) {
            position.setGeofenceIds(geofenceIds);
        }
        callback.processed(false);
    }

}
