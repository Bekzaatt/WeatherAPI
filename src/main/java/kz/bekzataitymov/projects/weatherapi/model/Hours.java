package kz.bekzataitymov.projects.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hours {
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime datetime;
    private double temp;
    @JsonProperty("feelslike")
    private double feelsLike;
    private double humidity;
    private double visibility;
    private double windspeed;
}
