
package org.traccar.notification;

import org.apache.velocity.VelocityContext;
import org.traccar.helper.model.UserUtil;
import org.traccar.model.Device;
import org.traccar.model.Driver;
import org.traccar.model.Event;
import org.traccar.model.Geofence;
import org.traccar.model.Maintenance;
import org.traccar.model.Notification;
import org.traccar.model.Position;
import org.traccar.model.Server;
import org.traccar.model.User;
import org.traccar.session.cache.CacheManager;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class NotificationFormatter {

    private final CacheManager cacheManager;
    private final TextTemplateFormatter textTemplateFormatter;

    @Inject
    public NotificationFormatter(
            CacheManager cacheManager, TextTemplateFormatter textTemplateFormatter) {
        this.cacheManager = cacheManager;
        this.textTemplateFormatter = textTemplateFormatter;
    }

    public NotificationMessage formatMessage(
            Notification notification, User user, Event event, Position position, String templatePath) {

        Server server = cacheManager.getServer();
        Device device = cacheManager.getObject(Device.class, event.getDeviceId());

        VelocityContext velocityContext = textTemplateFormatter.prepareContext(server, user);

        velocityContext.put("notification", notification);
        velocityContext.put("device", device);
        velocityContext.put("event", event);
        if (position != null) {
            velocityContext.put("position", position);
            velocityContext.put("speedUnit", UserUtil.getSpeedUnit(server, user));
            velocityContext.put("distanceUnit", UserUtil.getDistanceUnit(server, user));
            velocityContext.put("volumeUnit", UserUtil.getVolumeUnit(server, user));
        }
        if (event.getGeofenceId() != 0) {
            velocityContext.put("geofence", cacheManager.getObject(Geofence.class, event.getGeofenceId()));
        }
        if (event.getMaintenanceId() != 0) {
            velocityContext.put("maintenance", cacheManager.getObject(Maintenance.class, event.getMaintenanceId()));
        }
        String driverUniqueId = event.getString(Position.KEY_DRIVER_UNIQUE_ID);
        if (driverUniqueId != null) {
            velocityContext.put("driver", cacheManager.getDeviceObjects(device.getId(), Driver.class).stream()
                    .filter(driver -> driver.getUniqueId().equals(driverUniqueId)).findFirst().orElse(null));
        }

        return textTemplateFormatter.formatMessage(velocityContext, event.getType(), templatePath);
    }

}
