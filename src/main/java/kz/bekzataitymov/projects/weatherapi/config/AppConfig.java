package kz.bekzataitymov.projects.weatherapi.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public WebClientProps webClientProps(){
        return new WebClientProps();
    }

    @Bean
    public WebClient webClient(WebClientProps webClientProps){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProps.getConnectionTimeout())
                .responseTimeout(Duration.ofMillis(webClientProps.getResponseTimeout()));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
