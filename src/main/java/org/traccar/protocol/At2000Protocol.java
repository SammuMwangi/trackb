
package org.traccar.protocol;

import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;
import org.traccar.config.Config;

import jakarta.inject.Inject;

public class At2000Protocol extends BaseProtocol {

    @Inject
    public At2000Protocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new At2000FrameDecoder());
                pipeline.addLast(new At2000ProtocolDecoder(At2000Protocol.this));
            }
        });
    }

}
