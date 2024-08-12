
package org.traccar.handler.events;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.traccar.config.Keys;
import org.traccar.helper.model.AttributeUtil;
import org.traccar.helper.model.PositionUtil;
import org.traccar.model.Device;
import org.traccar.model.Position;
import org.traccar.reports.common.TripsConfig;
import org.traccar.session.cache.CacheManager;
import org.traccar.session.state.MotionProcessor;
import org.traccar.session.state.MotionState;
import org.traccar.storage.Storage;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Request;

public class MotionEventHandler extends BaseEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MotionEventHandler.class);

    private final CacheManager cacheManager;
    private final Storage storage;

    @Inject
    public MotionEventHandler(CacheManager cacheManager, Storage storage) {
        this.cacheManager = cacheManager;
        this.storage = storage;
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {

        long deviceId = position.getDeviceId();
        Device device = cacheManager.getObject(Device.class, deviceId);
        if (device == null || !PositionUtil.isLatest(cacheManager, position)) {
            return;
        }
        boolean processInvalid = AttributeUtil.lookup(
                cacheManager, Keys.EVENT_MOTION_PROCESS_INVALID_POSITIONS, deviceId);
        if (!processInvalid && !position.getValid()) {
            return;
        }

        TripsConfig tripsConfig = new TripsConfig(new AttributeUtil.CacheProvider(cacheManager, deviceId));
        MotionState state = MotionState.fromDevice(device);
        MotionProcessor.updateState(state, position, position.getBoolean(Position.KEY_MOTION), tripsConfig);
        if (state.isChanged()) {
            state.toDevice(device);
            try {
                storage.updateObject(device, new Request(
                        new Columns.Include("motionStreak", "motionState", "motionTime", "motionDistance"),
                        new Condition.Equals("id", device.getId())));
            } catch (StorageException e) {
                LOGGER.warn("Update device motion error", e);
            }
        }
        if (state.getEvent() != null) {
            callback.eventDetected(state.getEvent());
        }
    }

}
