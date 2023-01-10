package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.shareit.item.exception.ChangingItemOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ErrorHandler {
    @Nullable
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorEntity> sqlException(final SQLException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @Nullable
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorEntity> validationException(final ConstraintViolationException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @Nullable
    @ExceptionHandler(ShareItException.class)
    public ResponseEntity<ErrorEntity> shareItException(final ShareItException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @Nullable
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorEntity> itemNotFoundException(final ItemNotFoundException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @Nullable
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorEntity> userNotFoundException(final UserNotFoundException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @Nullable
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorEntity> userAlreadyExistsException(final UserAlreadyExistsException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.CONFLICT, request);
    }

    @Nullable
    @ExceptionHandler(ChangingItemOwnerException.class)
    public ResponseEntity<ErrorEntity> changingItemOwnerException(final ChangingItemOwnerException e, WebRequest request) {
        return getErrorEntity(e.getMessage(), HttpStatus.FORBIDDEN, request);
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
