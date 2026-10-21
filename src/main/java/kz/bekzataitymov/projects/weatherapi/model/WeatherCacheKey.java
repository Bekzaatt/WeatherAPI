package kz.bekzataitymov.projects.weatherapi.model;

import java.time.LocalDate;

public record WeatherCacheKey(
        String city,
        LocalDate fromDate,
        LocalDate toDate
) {
}
