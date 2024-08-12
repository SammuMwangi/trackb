
package org.traccar.schedule;

public abstract class SingleScheduleTask implements ScheduleTask {
    @Override
    public boolean multipleInstances() {
        return false;
    }
}
