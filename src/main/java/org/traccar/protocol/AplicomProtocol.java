
package org.traccar.protocol;

import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;
import org.traccar.config.Config;

import jakarta.inject.Inject;

public class AplicomProtocol extends BaseProtocol {

    @Inject
    public AplicomProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new AplicomFrameDecoder());
                pipeline.addLast(new AplicomProtocolDecoder(AplicomProtocol.this));
            }
        });
    }

}
