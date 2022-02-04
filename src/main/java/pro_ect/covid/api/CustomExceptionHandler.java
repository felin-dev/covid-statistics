package pro_ect.covid.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handle(Exception e) {
        return handleException(e, INTERNAL_SERVER_ERROR, "Unspecified error occurred.");
    }

    @ExceptionHandler(ErrorResponseStatusException.class)
    ResponseEntity<ErrorResponse> handle(ErrorResponseStatusException e) {
        return handleException(e, e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
        return handleException(e, BAD_REQUEST, e.getMessage());
    }

    private static ResponseEntity<ErrorResponse> handleException(Exception e, HttpStatus status, String message) {
        UUID requestId = randomUUID();
        log.error("Request '{}' failed: ", requestId, e);

        return ResponseEntity.status(status).body(new ErrorResponse(requestId, message));
    }

    private record ErrorResponse(UUID requestId, String message) {
    }
}