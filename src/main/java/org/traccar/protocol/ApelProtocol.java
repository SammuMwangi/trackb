
package org.traccar.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;
import org.traccar.config.Config;

import java.nio.ByteOrder;
import jakarta.inject.Inject;

public class ApelProtocol extends BaseProtocol {

    @Inject
    public ApelProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 1024, 2, 2, 4, 0, true));
                pipeline.addLast(new ApelProtocolDecoder(ApelProtocol.this));
            }
        });
    }

}
