
package org.traccar.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.server.JettyWebSocketServlet;
import org.eclipse.jetty.websocket.server.JettyWebSocketServletFactory;
import org.traccar.api.resource.SessionResource;
import org.traccar.api.security.LoginService;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import org.traccar.session.ConnectionManager;
import org.traccar.storage.Storage;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpSession;
import org.traccar.storage.StorageException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.List;

@Singleton
public class AsyncSocketServlet extends JettyWebSocketServlet {

    private final Config config;
    private final ObjectMapper objectMapper;
    private final ConnectionManager connectionManager;
    private final Storage storage;
    private final LoginService loginService;

    @Inject
    public AsyncSocketServlet(
            Config config, ObjectMapper objectMapper, ConnectionManager connectionManager, Storage storage,
            LoginService loginService) {
        this.config = config;
        this.objectMapper = objectMapper;
        this.connectionManager = connectionManager;
        this.storage = storage;
        this.loginService = loginService;
    }

    @Override
    public void configure(JettyWebSocketServletFactory factory) {
        factory.setIdleTimeout(Duration.ofMillis(config.getLong(Keys.WEB_TIMEOUT)));
        factory.setCreator((req, resp) -> {
            Long userId = null;
            List<String> tokens = req.getParameterMap().get("token");
            if (tokens != null && !tokens.isEmpty()) {
                String token = tokens.iterator().next();
                try {
                    userId = loginService.login(token).getUser().getId();
                } catch (StorageException | GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (req.getSession() != null) {
                userId = (Long) ((HttpSession) req.getSession()).getAttribute(SessionResource.USER_ID_KEY);
            }
            if (userId != null) {
                return new AsyncSocket(objectMapper, connectionManager, storage, userId);
            }
            return null;
        });
    }

}
