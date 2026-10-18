package kz.bekzataitymov.projects.weatherapi.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import kz.bekzataitymov.projects.weatherapi.codec.WeatherModelCodec;
import kz.bekzataitymov.projects.weatherapi.codec.WeatherModelPredictCodec;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelDataRange;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelPredict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RedisConfig {
    private RedisClient redisClient = null;
    private StatefulRedisConnection<String, WeatherModelDataRange> connection = null;
    private StatefulRedisConnection<String, WeatherModelPredict> connectionPredict = null;

    @PostConstruct
    public StatefulRedisConnection<String, WeatherModelDataRange> getRedis(){
        RedisURI redisURI = RedisURI.Builder
                .redis("localhost", 6379)
                .withTimeout(Duration.ofSeconds(20))
                .build();

        redisClient = RedisClient.create(redisURI);
        connection = redisClient.connect(new WeatherModelCodec());
        connectionPredict = redisClient.connect(new WeatherModelPredictCodec());
        return connection;
    }

    @Bean
    public RedisCommands<String, WeatherModelDataRange> getRedisCommands(){
        return connection.sync();
    }

    @Bean
    public RedisCommands<String, WeatherModelPredict> getRedisCommandsPredict(){
        return connectionPredict.sync();
    }

    @PreDestroy
    private void destroy(){
        if(redisClient != null){
            redisClient.shutdown();
        }
        if(connection != null){
            connection.close();
        }
    }

}
