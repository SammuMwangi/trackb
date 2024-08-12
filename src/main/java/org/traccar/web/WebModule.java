
package org.traccar.web;

import com.google.inject.servlet.ServletModule;
import org.traccar.api.AsyncSocketServlet;
import org.traccar.api.MediaFilter;

public class WebModule extends ServletModule {

    @Override
    protected void configureServlets() {
        filter("/*").through(OverrideFilter.class);
        filter("/api/*").through(ThrottlingFilter.class);
        filter("/api/media/*").through(MediaFilter.class);
        serve("/api/socket").with(AsyncSocketServlet.class);
    }
}
