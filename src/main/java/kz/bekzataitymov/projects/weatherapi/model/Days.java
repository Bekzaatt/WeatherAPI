package kz.bekzataitymov.projects.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Days {
    private String conditions;
    private String description;
    @JsonProperty("datetime")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTime;
    private double temp;
    @JsonProperty("feelslike")
    private double feelsLike;
    private double humidity;
    private double windspeed;
    private List<Hours> hours;
}
