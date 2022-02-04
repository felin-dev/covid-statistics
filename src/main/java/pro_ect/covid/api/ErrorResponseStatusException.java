package pro_ect.covid.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ErrorResponseStatusException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public ErrorResponseStatusException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
