package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RestrictedBookingAccessException extends ShareItException {
    public RestrictedBookingAccessException(String message) {
        super(message);
    }
}
