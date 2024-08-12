
package org.traccar.forward;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.traccar.config.Config;
import org.traccar.config.Keys;

import java.io.IOException;

public class PositionForwarderAmqp implements PositionForwarder {

    private final AmqpClient amqpClient;
    private final ObjectMapper objectMapper;

    public PositionForwarderAmqp(Config config, ObjectMapper objectMapper) {
        String connectionUrl = config.getString(Keys.FORWARD_URL);
        String exchange = config.getString(Keys.FORWARD_EXCHANGE);
        String topic = config.getString(Keys.FORWARD_TOPIC);
        amqpClient = new AmqpClient(connectionUrl, exchange, topic);
        this.objectMapper = objectMapper;
    }

    @Override
    public void forward(PositionData positionData, ResultHandler resultHandler) {
        try {
            String value = objectMapper.writeValueAsString(positionData);
            amqpClient.publishMessage(value);
            resultHandler.onResult(true, null);
        } catch (IOException e) {
            resultHandler.onResult(false, e);
        }
    }
}
