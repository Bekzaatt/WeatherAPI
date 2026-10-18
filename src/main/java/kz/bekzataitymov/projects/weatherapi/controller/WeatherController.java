package kz.bekzataitymov.projects.weatherapi.controller;

import kz.bekzataitymov.projects.weatherapi.model.WeatherModelDataRange;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelPredict;
import kz.bekzataitymov.projects.weatherapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/timeRange/{city}/{fromData}/{toData}")
    public WeatherModelDataRange getWeatherDataByRange(@PathVariable String city,
                                                       @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromData,
                                                       @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toData) {
        return this.weatherService.getWeatherDataByRange(city, fromData, toData);
    }

    @GetMapping("/forecast/{city}")
    public WeatherModelPredict getWeatherDataForecast(@PathVariable String city) {
        return this.weatherService.getWeatherDataForecast(city);
    }





}
