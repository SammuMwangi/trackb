
package org.traccar.handler.events;

import org.traccar.model.Event;
import org.traccar.model.Position;

public abstract class BaseEventHandler {

    public interface Callback {
        void eventDetected(Event event);
    }

    /**
     * Event handlers should be processed synchronously.
     */
    public abstract void analyzePosition(Position position, Callback callback);
}
