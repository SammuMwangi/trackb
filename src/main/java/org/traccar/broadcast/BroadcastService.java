
package org.traccar.broadcast;

import org.traccar.LifecycleObject;

public interface BroadcastService extends LifecycleObject, BroadcastInterface {
    boolean singleInstance();
    void registerListener(BroadcastInterface listener);
}
