package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemNotAvailableException extends ShareItException {
    public ItemNotAvailableException() {
        super("Item is not available to booking");
    }
}
