
package org.traccar.api;

import org.traccar.model.BaseModel;
import org.traccar.model.User;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Request;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import java.util.Collection;
import java.util.LinkedList;

public class SimpleObjectResource<T extends BaseModel> extends BaseObjectResource<T> {

    public SimpleObjectResource(Class<T> baseClass) {
        super(baseClass);
    }

    @GET
    public Collection<T> get(
            @QueryParam("all") boolean all, @QueryParam("userId") long userId) throws StorageException {

        var conditions = new LinkedList<Condition>();

        if (all) {
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), baseClass));
            }
        } else {
            if (userId == 0) {
                userId = getUserId();
            } else {
                permissionsService.checkUser(getUserId(), userId);
            }
            conditions.add(new Condition.Permission(User.class, userId, baseClass));
        }

        return storage.getObjects(baseClass, new Request(new Columns.All(), Condition.merge(conditions)));
    }

}
