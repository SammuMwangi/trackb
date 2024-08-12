
package org.traccar.forward;

public interface PositionForwarder {
    void forward(PositionData positionData, ResultHandler resultHandler);
}
