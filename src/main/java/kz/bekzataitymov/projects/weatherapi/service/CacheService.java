package kz.bekzataitymov.projects.weatherapi.service;

import kz.bekzataitymov.projects.weatherapi.model.WeatherCacheKey;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelDataRange;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelPredict;
import kz.bekzataitymov.projects.weatherapi.repository.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringMapMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final CacheRepository cacheRepository;
    private final Logger logger = LogManager.getLogger(CacheService.class);

    public WeatherModelDataRange get(WeatherCacheKey cacheKey) {
        WeatherModelDataRange weatherModel= this.cacheRepository.get(cacheKey);
        if (weatherModel != null) {
            this.logger.info(new StringMapMessage()
                    .with("message", "successfully returned from cache"));
            return weatherModel;
        } else {
            this.logger.info(new StringMapMessage()
                    .with("message", "no data in the cache"));
        }
        return null;
    }

    public WeatherModelPredict getPredictCache(WeatherCacheKey cacheKey) {
        WeatherModelPredict weatherModel = this.cacheRepository.getPredict(cacheKey);
        if (weatherModel != null) {
            this.logger.info(new StringMapMessage()
                    .with("message", "successfully returned from cache"));
            return weatherModel;
        } else {
            this.logger.info(new StringMapMessage()
                    .with("message", "no data in the cache"));
        }
        return null;
    }

    public void put(WeatherCacheKey cacheKey, WeatherModelDataRange weatherModel) {
        this.cacheRepository.put(cacheKey, weatherModel);
    }

    public void put(WeatherCacheKey cacheKey, WeatherModelPredict weatherModel) {
        this.cacheRepository.put(cacheKey, weatherModel);
    }
}
