
package org.traccar.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;
import org.traccar.config.Config;

import java.nio.ByteOrder;

import jakarta.inject.Inject;

public class AnytrekProtocol extends BaseProtocol {

    @Inject
    public AnytrekProtocol(Config config) {
        addServer(new TrackerServer(config, getName(), false) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline, Config config) {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 1024, 2, 2, 2, 0, true));
                pipeline.addLast(new AnytrekProtocolDecoder(AnytrekProtocol.this));
            }
        });
    }

}
