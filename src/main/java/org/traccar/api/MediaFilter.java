
package org.traccar.api;

import com.google.inject.Provider;
import org.traccar.api.resource.SessionResource;
import org.traccar.api.security.PermissionsService;
import org.traccar.database.StatisticsManager;
import org.traccar.helper.Log;
import org.traccar.model.Device;
import org.traccar.storage.Storage;
import org.traccar.storage.StorageException;
import org.traccar.storage.query.Columns;
import org.traccar.storage.query.Condition;
import org.traccar.storage.query.Request;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class MediaFilter implements Filter {

    private final Storage storage;
    private final StatisticsManager statisticsManager;
    private final Provider<PermissionsService> permissionsServiceProvider;

    @Inject
    public MediaFilter(
            Storage storage, StatisticsManager statisticsManager,
            Provider<PermissionsService> permissionsServiceProvider) {
        this.storage = storage;
        this.statisticsManager = statisticsManager;
        this.permissionsServiceProvider = permissionsServiceProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            Long userId = null;
            if (session != null) {
                userId = (Long) session.getAttribute(SessionResource.USER_ID_KEY);
                if (userId != null) {
                    statisticsManager.registerRequest(userId);
                }
            }
            if (userId == null) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String path = ((HttpServletRequest) request).getPathInfo();
            String[] parts = path != null ? path.split("/") : null;
            if (parts != null && parts.length >= 2) {
                Device device = storage.getObject(Device.class, new Request(
                        new Columns.All(), new Condition.Equals("uniqueId", parts[1])));
                if (device != null) {
                    permissionsServiceProvider.get().checkPermission(Device.class, userId, device.getId());
                    chain.doFilter(request, response);
                    return;
                }
            }

            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SecurityException | StorageException e) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().println(Log.exceptionStack(e));
        }
    }

}
