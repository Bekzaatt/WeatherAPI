package kz.bekzataitymov.projects.weatherapi.codec;

import io.lettuce.core.codec.RedisCodec;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelDataRange;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class WeatherModelCodec implements RedisCodec<String, WeatherModelDataRange> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String decodeKey(ByteBuffer byteBuffer) {
        return StandardCharsets.UTF_8.decode(byteBuffer).toString();
    }

    @Override
    public WeatherModelDataRange decodeValue(ByteBuffer byteBuffer) {
        String json = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        try {
            return OBJECT_MAPPER.readValue(json, WeatherModelDataRange.class);
        } catch (JacksonException exception) {
            throw new RuntimeException("Failed to deserialize value from Redis.", exception);
        }
    }

    @Override
    public ByteBuffer encodeKey(String s) {
        return ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public ByteBuffer encodeValue(WeatherModelDataRange weatherModel) {
        try {
            String weatherJson = OBJECT_MAPPER.writeValueAsString(weatherModel);
            return ByteBuffer.wrap(weatherJson.getBytes(StandardCharsets.UTF_8));
        } catch (JacksonException exception) {
            throw new RuntimeException("Failed to serialize value to Redis", exception);
        }
    }
}
