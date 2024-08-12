
package org.traccar.handler;

import jakarta.inject.Inject;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import org.traccar.helper.model.AttributeUtil;
import org.traccar.model.Position;
import org.traccar.session.cache.CacheManager;

public class CopyAttributesHandler extends BasePositionHandler {

    private final CacheManager cacheManager;

    @Inject
    public CopyAttributesHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        String attributesString = AttributeUtil.lookup(
                cacheManager, Keys.PROCESSING_COPY_ATTRIBUTES, position.getDeviceId());
        Position last = cacheManager.getPosition(position.getDeviceId());
        if (last != null && attributesString != null) {
            for (String attribute : attributesString.split("[ ,]")) {
                if (last.hasAttribute(attribute) && !position.hasAttribute(attribute)) {
                    position.getAttributes().put(attribute, last.getAttributes().get(attribute));
                }
            }
        }
        callback.processed(false);
    }

}
