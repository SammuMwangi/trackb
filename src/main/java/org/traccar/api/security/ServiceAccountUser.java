
package org.traccar.api.security;

import org.traccar.model.User;

public class ServiceAccountUser extends User {

    public static final long ID = 9000000000000000000L;

    public ServiceAccountUser() {
        setId(ID);
        setName("Service Account");
        setEmail("none");
        setAdministrator(true);
    }
}
