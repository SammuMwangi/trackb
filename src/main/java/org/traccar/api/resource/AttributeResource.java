
package org.traccar.api.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.traccar.api.ExtendedObjectResource;
import org.traccar.model.Attribute;
import org.traccar.model.Device;
import org.traccar.model.Position;
import org.traccar.handler.ComputedAttributesHandler;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Request;

@Path("attributes/computed")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttributeResource extends ExtendedObjectResource<Attribute> {

    @Inject
    private ComputedAttributesHandler computedAttributesHandler;

    public AttributeResource() {
        super(Attribute.class);
    }

    @POST
    @Path("test")
    public Response test(@QueryParam("deviceId") long deviceId, Attribute entity) throws StorageException {
        permissionsService.checkAdmin(getUserId());
        permissionsService.checkPermission(Device.class, getUserId(), deviceId);

        Position position = storage.getObject(Position.class, new Request(
                new Columns.All(),
                new Condition.LatestPositions(deviceId)));

        Object result = computedAttributesHandler.computeAttribute(entity, position);
        if (result != null) {
            return switch (entity.getType()) {
                case "number", "boolean" -> Response.ok(result).build();
                default -> Response.ok(result.toString()).build();
            };
        } else {
            return Response.noContent().build();
        }
    }

    @POST
    public Response add(Attribute entity) throws Exception {
        permissionsService.checkAdmin(getUserId());
        return super.add(entity);
    }

    @Path("{id}")
    @PUT
    public Response update(Attribute entity) throws Exception {
        permissionsService.checkAdmin(getUserId());
        return super.update(entity);
    }

    @Path("{id}")
    @DELETE
    public Response remove(@PathParam("id") long id) throws Exception {
        permissionsService.checkAdmin(getUserId());
        return super.remove(id);
    }

}
