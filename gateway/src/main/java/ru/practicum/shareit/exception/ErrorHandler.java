package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ErrorHandler {

    @Nullable
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorEntity> illegalArgumentException(final IllegalArgumentException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorEntity> getErrorEntity(String message, HttpStatus status, WebRequest request) {
        HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();

        return new ResponseEntity<>(ErrorEntity.builder()
                .dateTime(ZonedDateTime.now(ZoneId.of("Europe/Moscow")))
                .status(status.value())
                .error(message)
                .url(httpServletRequest.getRequestURI())
                .method(httpServletRequest.getMethod())
                .build(),
                status
        );
    }
}
