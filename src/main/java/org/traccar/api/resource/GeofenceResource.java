
package org.traccar.api.resource;

import org.traccar.api.ExtendedObjectResource;
import org.traccar.model.Geofence;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("geofences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeofenceResource extends ExtendedObjectResource<Geofence> {

    public GeofenceResource() {
        super(Geofence.class);
    }

}
