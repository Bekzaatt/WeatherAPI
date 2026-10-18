package kz.bekzataitymov.projects.weatherapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherModelPredict{
    private String address;
    private String description;
    private List<Days> days;
    private double latitude;
    private double longitude;
}
