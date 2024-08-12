
package org.traccar.schedule;

import jakarta.inject.Inject;
import org.traccar.broadcast.BroadcastService;
import org.traccar.helper.model.DeviceUtil;
import org.traccar.storage.Storage;
import org.traccar.storage.StorageException;

import java.util.concurrent.ScheduledExecutorService;

public class TaskClearStatus implements ScheduleTask {

    @Inject
    public TaskClearStatus(BroadcastService broadcastService, Storage storage) throws StorageException {
        if (broadcastService.singleInstance()) {
            DeviceUtil.resetStatus(storage);
        }
    }

    @Override
    public void schedule(ScheduledExecutorService executor) {
    }

    @Override
    public void run() {
    }

}
