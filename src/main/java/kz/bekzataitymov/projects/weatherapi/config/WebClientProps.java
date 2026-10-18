package kz.bekzataitymov.projects.weatherapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("weather.api")
public class WebClientProps {
    private String url;
    private String key;
    private int connectionTimeout;
    private int responseTimeout;
}
