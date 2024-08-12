
package org.traccar.api;

import org.traccar.api.security.PermissionsService;
import org.traccar.api.security.UserPrincipal;
import org.traccar.storage.Storage;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

public class BaseResource {

    @Context
    private SecurityContext securityContext;

    @Inject
    protected Storage storage;

    @Inject
    protected PermissionsService permissionsService;

    protected long getUserId() {
        UserPrincipal principal = (UserPrincipal) securityContext.getUserPrincipal();
        if (principal != null) {
            return principal.getUserId();
        }
        return 0;
    }

}
