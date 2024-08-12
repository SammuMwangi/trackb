
package org.traccar.handler;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.model.Position;
import org.traccar.speedlimit.SpeedLimitProvider;

public class SpeedLimitHandler extends BasePositionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedLimitHandler.class);

    private final SpeedLimitProvider speedLimitProvider;

    @Inject
    public SpeedLimitHandler(SpeedLimitProvider speedLimitProvider) {
        this.speedLimitProvider = speedLimitProvider;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {

        speedLimitProvider.getSpeedLimit(position.getLatitude(), position.getLongitude(),
                new SpeedLimitProvider.SpeedLimitProviderCallback() {
            @Override
            public void onSuccess(double speedLimit) {
                position.set(Position.KEY_SPEED_LIMIT, speedLimit);
                callback.processed(false);
            }

            @Override
            public void onFailure(Throwable e) {
                LOGGER.warn("Speed limit provider failed", e);
                callback.processed(false);
            }
        });
    }

}
