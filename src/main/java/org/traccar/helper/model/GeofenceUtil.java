
package org.traccar.helper.model;

import org.traccar.config.Config;
import org.traccar.model.Geofence;
import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

public final class GeofenceUtil {

    private GeofenceUtil() {
    }

    public static List<Long> getCurrentGeofences(Config config, CacheManager cacheManager, Position position) {
        List<Long> result = new ArrayList<>();
        for (Geofence geofence : cacheManager.getDeviceObjects(position.getDeviceId(), Geofence.class)) {
            if (geofence.getGeometry().containsPoint(
                    config, geofence, position.getLatitude(), position.getLongitude())) {
                result.add(geofence.getId());
            }
        }
        return result;
    }

}
