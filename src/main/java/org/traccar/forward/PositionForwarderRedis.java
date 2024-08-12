
package org.traccar.forward;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.traccar.config.Config;
import org.traccar.config.Keys;
import redis.clients.jedis.Jedis;

public class PositionForwarderRedis implements PositionForwarder {

    private final String url;

    private final ObjectMapper objectMapper;

    public PositionForwarderRedis(Config config, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.url = config.getString(Keys.FORWARD_URL);
    }

    @Override
    public void forward(PositionData positionData, ResultHandler resultHandler) {

        try {
            String key = "positions." + positionData.getDevice().getUniqueId();
            String value = objectMapper.writeValueAsString(positionData.getPosition());
            try (Jedis jedis = new Jedis(url)) {
                jedis.lpush(key, value);
            }
            resultHandler.onResult(true, null);
        } catch (JsonProcessingException e) {
            resultHandler.onResult(false, e);
        }
    }

}
