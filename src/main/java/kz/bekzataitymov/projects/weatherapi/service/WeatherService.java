package kz.bekzataitymov.projects.weatherapi.service;

import kz.bekzataitymov.projects.weatherapi.exception.BadRequestException;
import kz.bekzataitymov.projects.weatherapi.model.WeatherCacheKey;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelDataRange;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelPredict;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WebClientService webClientService;
    private final CacheService cacheService;

    public WeatherModelDataRange getWeatherDataByRange(String city, LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(LocalDate.now()) || toDate.isAfter(LocalDate.now()) ||
                (fromDate.isAfter(toDate)) || city == null || city.isBlank()) {
            throw new BadRequestException("Invalid datetime");
        }

        WeatherCacheKey cacheKey = new WeatherCacheKey(city, fromDate, toDate);
        WeatherModelDataRange weatherModel = this.cacheService.get(cacheKey);
        if (weatherModel == null) {
            weatherModel = this.webClientService.getWeatherByTimeRange(city, fromDate, toDate);
            this.cacheService.put(cacheKey, weatherModel);
            return weatherModel;
        } else {
            return weatherModel;
        }
    }

    public WeatherModelPredict getWeatherDataForecast(String city) {
        if (city == null || city.isBlank()) {
            throw new BadRequestException("City is required");
        }

        WeatherCacheKey weatherCacheKey = new WeatherCacheKey(city, null, null);
        WeatherModelPredict weatherModel = cacheService.getPredictCache(weatherCacheKey);
        if (weatherModel == null) {
            weatherModel = this.webClientService.getWeatherForecast(city);
            this.cacheService.put(weatherCacheKey, weatherModel);
            return weatherModel;
        } else {

            return weatherModel;
        }
    }
}
