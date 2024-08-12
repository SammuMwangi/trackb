
package org.traccar.reports.common;

import org.traccar.storage.StorageException;

import java.io.IOException;
import java.io.OutputStream;

public interface ReportExecutor {
    void execute(OutputStream stream) throws StorageException, IOException;
}
