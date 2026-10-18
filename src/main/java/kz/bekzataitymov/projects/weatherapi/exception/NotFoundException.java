package kz.bekzataitymov.projects.weatherapi.exception;

public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException(String message) {
        this.message = message;
    }
}
