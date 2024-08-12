
package org.traccar.forward;

public interface EventForwarder {
    void forward(EventData eventData, ResultHandler resultHandler);
}
