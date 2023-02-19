package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OwnerBookingException extends ShareItException {
    public OwnerBookingException() {
        super("Attempt to book owning item");
    }
}
