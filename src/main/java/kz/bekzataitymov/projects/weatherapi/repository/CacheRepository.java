package kz.bekzataitymov.projects.weatherapi.repository;

import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.sync.RedisCommands;
import kz.bekzataitymov.projects.weatherapi.model.WeatherCacheKey;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelDataRange;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelPredict;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CacheRepository {
    private final Logger logger = LogManager.getLogger(CacheRepository.class);
    private final RedisCommands<String, WeatherModelDataRange> redisCommands;
    private final RedisCommands<String, WeatherModelPredict> redisCommandsPredict;
    private static final String KEY = "weather:%s:%s:%s";
    private static final String KEY_FORECAST = "weather:%s";

    public WeatherModelDataRange get(WeatherCacheKey cacheKey){
         try {
             return redisCommands
                     .get(String.format(KEY, cacheKey.city(), cacheKey.fromDate(), cacheKey.toDate()));
         } catch (RedisConnectionException | RedisCommandTimeoutException exception) {
             logger.error("Redis error", exception);
             return null;
         }
    }

    public WeatherModelPredict getPredict(WeatherCacheKey cacheKey){
        try {
            return redisCommandsPredict
                    .get(String.format(KEY_FORECAST, cacheKey.city()));
        } catch (RedisConnectionException | RedisCommandTimeoutException exception) {
            logger.error("Cache get error", exception);
            return null;
        }
    }

    public void put(WeatherCacheKey cacheKey, WeatherModelDataRange weatherModel) {
        try {
            this.redisCommands
                    .set(String.format(KEY, cacheKey.city(), cacheKey.fromDate(), cacheKey.toDate()), weatherModel, SetArgs.Builder.ex(10800));
        } catch (Exception e) {
            logger.error("Cache put error", e);
        }
    }

    public void put(WeatherCacheKey cacheKey, WeatherModelPredict weatherModel) {
        try {
            this.redisCommandsPredict
                    .set(String.format(KEY_FORECAST, cacheKey.city()), weatherModel, SetArgs.Builder.ex(10800));
        } catch (Exception e) {
            logger.error("Cache put error", e);
        }
    }
}
