package kz.bekzataitymov.projects.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class WeatherModelDataRange {
    private String address;
    private List<Days> days;
    private double latitude;
    private double longitude;
}
