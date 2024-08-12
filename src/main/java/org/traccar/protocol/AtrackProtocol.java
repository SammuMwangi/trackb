
package org.traccar.protocol;

import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;
import org.traccar.config.Config;
import org.traccar.model.Command;

import jakarta.inject.Inject;

public class AtrackProtocol extends BaseProtocol {

    @Inject
    public AtrackProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_CUSTOM);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new AtrackFrameDecoder());
                pipeline.addLast(new AtrackProtocolEncoder(AtrackProtocol.this));
                pipeline.addLast(new AtrackProtocolDecoder(AtrackProtocol.this));
            }
        });
        addServer(new TrackerServer(config, getName(), true) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new AtrackProtocolEncoder(AtrackProtocol.this));
                pipeline.addLast(new AtrackProtocolDecoder(AtrackProtocol.this));
            }
        });
    }

}
