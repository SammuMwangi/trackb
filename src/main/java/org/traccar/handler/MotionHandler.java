
package org.traccar.handler;

import jakarta.inject.Inject;
import org.traccar.config.Keys;
import org.traccar.helper.model.AttributeUtil;
import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;

public class MotionHandler extends BasePositionHandler {

    private final CacheManager cacheManager;

    @Inject
    public MotionHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if (!position.hasAttribute(Position.KEY_MOTION)) {
            double threshold = AttributeUtil.lookup(
                    cacheManager, Keys.EVENT_MOTION_SPEED_THRESHOLD, position.getDeviceId());
            position.set(Position.KEY_MOTION, position.getSpeed() > threshold);
        }
        callback.processed(false);
    }

}
