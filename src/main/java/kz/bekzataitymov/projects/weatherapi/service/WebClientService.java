package kz.bekzataitymov.projects.weatherapi.service;

import io.netty.handler.timeout.ReadTimeoutException;
import kz.bekzataitymov.projects.weatherapi.config.WebClientProps;
import kz.bekzataitymov.projects.weatherapi.exception.BadRequestException;
import kz.bekzataitymov.projects.weatherapi.exception.NotFoundException;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelDataRange;
import kz.bekzataitymov.projects.weatherapi.model.WeatherModelPredict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebClientService {
    private final WebClient webClient;
    private final WebClientProps webClientProps;

    public WeatherModelDataRange getWeatherByTimeRange(String city, LocalDate fromDate, LocalDate toDate) {
        return this.webClient.get()
                .uri(this.webClientProps.getUrl() + "/{city}/{fromDate}/{toDate}?key={key}", city, fromDate, toDate, this.webClientProps.getKey())
                .retrieve()
                .onStatus(status -> status.value() == 404, throwable ->
                        throwable.bodyToMono(String.class).map(NotFoundException::new))
                .onStatus(status -> status.value() == 400, throwable ->
                        throwable.bodyToMono(String.class).map(BadRequestException::new))
                .onStatus(HttpStatusCode::is4xxClientError, throwable ->
                        throwable.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(WeatherModelDataRange.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500))
                        .filter(throwable -> throwable instanceof InternalException ||
                                throwable instanceof WebClientRequestException ||
                                throwable.getCause() instanceof ReadTimeoutException)
                        .doAfterRetry(signal -> log.info("Total number of retries: {}", signal.totalRetries() + 1)))
                .doOnError(error -> {
                    if (error instanceof InternalException ||
                            error instanceof WebClientRequestException ||
                            error.getCause() instanceof ReadTimeoutException) {
                        log.error("Error in WebClient", error);
                    }
                })
                .block();
    }

    public WeatherModelPredict getWeatherForecast(String city) {
        return this.webClient.get()
                .uri(this.webClientProps.getUrl() + "/{city}?key={key}", city, this.webClientProps.getKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if(clientResponse.statusCode() == HttpStatus.NOT_FOUND){
                        Mono.error(new NotFoundException("Not found"));
                    }
                    else if(clientResponse.statusCode() == HttpStatus.BAD_REQUEST){
                       Mono.error(new BadRequestException("Bad request"));
                    }
                    throw new RuntimeException();
                })
                .bodyToMono(WeatherModelPredict.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(500))
                        .filter(throwable -> throwable instanceof InternalException ||
                                throwable instanceof WebClientRequestException ||
                                throwable.getCause() instanceof ReadTimeoutException)
                        .doAfterRetry(retrySignal ->
                                log.info("Total retires attempt: {}", retrySignal.totalRetries() + 1)))
                .doOnError(error -> {
                    if (error instanceof InternalException ||
                            error instanceof WebClientRequestException ||
                            error.getCause() instanceof ReadTimeoutException) {
                        log.error("Error in WebClient", error);
                    }
                })
                .block();
    }
}
