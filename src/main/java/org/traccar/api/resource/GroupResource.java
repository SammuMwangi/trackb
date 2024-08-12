
package org.traccar.api.resource;

import org.traccar.api.SimpleObjectResource;
import org.traccar.model.Group;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource extends SimpleObjectResource<Group> {

    public GroupResource() {
        super(Group.class);
    }

}
