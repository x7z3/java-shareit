package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingsNotFoundException extends ShareItException {
    public BookingsNotFoundException(BookingState state, Integer userId) {
        super(String.format("Couldn't find any booking with state %s and user ID=%s", state, userId));
    }
}
