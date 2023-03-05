package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends ShareItException {
    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(Integer bookingId) {
        super(String.format("Booking with ID=%s is not found in the DB", bookingId));
    }
}
