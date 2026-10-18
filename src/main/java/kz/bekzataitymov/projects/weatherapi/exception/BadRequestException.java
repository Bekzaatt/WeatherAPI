package kz.bekzataitymov.projects.weatherapi.exception;

public class BadRequestException extends RuntimeException{
    private String message;

    public BadRequestException(String message){
        super(message);
    }
}
