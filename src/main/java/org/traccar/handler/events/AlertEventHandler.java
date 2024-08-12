
package org.traccar.handler.events;

import jakarta.inject.Inject;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import org.traccar.model.Event;
import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;

public class AlertEventHandler extends BaseEventHandler {

    private final CacheManager cacheManager;
    private final boolean ignoreDuplicateAlerts;

    @Inject
    public AlertEventHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        ignoreDuplicateAlerts = config.getBoolean(Keys.EVENT_IGNORE_DUPLICATE_ALERTS);
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {
        Object alarm = position.getAttributes().get(Position.KEY_ALARM);
        if (alarm != null) {
            boolean ignoreAlert = false;
            if (ignoreDuplicateAlerts) {
                Position lastPosition = cacheManager.getPosition(position.getDeviceId());
                if (lastPosition != null && alarm.equals(lastPosition.getAttributes().get(Position.KEY_ALARM))) {
                    ignoreAlert = true;
                }
            }
            if (!ignoreAlert) {
                Event event = new Event(Event.TYPE_ALARM, position);
                event.set(Position.KEY_ALARM, (String) alarm);
                callback.eventDetected(event);
            }
        }
    }

}
