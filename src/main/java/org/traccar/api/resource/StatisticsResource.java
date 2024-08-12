
package org.traccar.api.resource;

import org.traccar.api.BaseResource;
import org.traccar.model.Statistics;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Order;
import org.traccar.storage.query.Request;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Date;

@Path("statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatisticsResource extends BaseResource {

    @GET
    public Collection<Statistics> get(
            @QueryParam("from") Date from, @QueryParam("to") Date to) throws StorageException {
        permissionsService.checkAdmin(getUserId());
        return storage.getObjects(Statistics.class, new Request(
                new Columns.All(),
                new Condition.Between("captureTime", "from", from, "to", to),
                new Order("captureTime")));
    }

}
