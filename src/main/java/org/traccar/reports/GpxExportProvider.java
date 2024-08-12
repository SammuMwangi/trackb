
package org.traccar.reports;

import org.traccar.helper.DateUtil;
import org.traccar.helper.model.PositionUtil;
import org.traccar.model.Device;
import org.traccar.storage.Storage;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Request;

import jakarta.inject.Inject;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class GpxExportProvider {

    private final Storage storage;

    @Inject
    public GpxExportProvider(Storage storage) {
        this.storage = storage;
    }

    public void generate(
            OutputStream outputStream, long deviceId, Date from, Date to) throws StorageException {

        var device = storage.getObject(Device.class, new Request(
                new Columns.All(), new Condition.Equals("id", deviceId)));
        var positions = PositionUtil.getPositions(storage, deviceId, from, to);

        try (PrintWriter writer = new PrintWriter(outputStream)) {
            writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.print("<gpx version=\"1.0\">");
            writer.print("<trk>");
            writer.print("<name>");
            writer.print(device.getName());
            writer.print("</name>");
            writer.print("<trkseg>");
            positions.forEach(position -> {
                writer.print("<trkpt lat=\"");
                writer.print(position.getLatitude());
                writer.print("\" lon=\"");
                writer.print(position.getLongitude());
                writer.print("\">");
                writer.print("<ele>");
                writer.print(position.getAltitude());
                writer.print("</ele>");
                writer.print("<time>");
                writer.print(DateUtil.formatDate(position.getFixTime()));
                writer.print("</time>");
                writer.print("</trkpt>");
            });
            writer.print("</trkseg>");
            writer.print("</trk>");
            writer.print("</gpx>");
        }
    }

}
