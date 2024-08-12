
package org.traccar.forward;

public interface ResultHandler {
    void onResult(boolean success, Throwable throwable);
}
