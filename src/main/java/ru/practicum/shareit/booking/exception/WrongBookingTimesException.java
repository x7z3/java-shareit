package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongBookingTimesException extends ShareItException {
    public WrongBookingTimesException(String message) {
        super(message);
    }
}
