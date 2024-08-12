
package org.traccar.protocol;

import io.netty.handler.codec.string.StringEncoder;
import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;
import org.traccar.config.Config;
import org.traccar.model.Command;

import jakarta.inject.Inject;

public class AdmProtocol extends BaseProtocol {

    @Inject
    public AdmProtocol(Config config) {
        setSupportedDataCommands(
                Command.TYPE_GET_DEVICE_STATUS,
                Command.TYPE_CUSTOM);
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new AdmFrameDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new AdmProtocolEncoder(AdmProtocol.this));
                pipeline.addLast(new AdmProtocolDecoder(AdmProtocol.this));
            }
        });
    }

}
