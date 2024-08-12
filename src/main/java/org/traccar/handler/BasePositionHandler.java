
package org.traccar.handler;

import org.traccar.model.Position;

public abstract class BasePositionHandler {

    public interface Callback {
        void processed(boolean filtered);
    }

    public abstract void handlePosition(Position position, Callback callback);
}
