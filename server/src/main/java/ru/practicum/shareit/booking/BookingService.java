package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto bookItem(BookingDto bookingDto, Integer integer);

    BookingDto approveBooking(Integer bookingId, boolean approved, Integer userId);

    List<BookingDto> getBookings(BookingState state, Integer userId, Integer from, Integer size);

    BookingDto getBooking(Integer bookingId, Integer userId);

    List<BookingDto> getOwnerBookings(BookingState state, Integer userId, Integer from, Integer size);
}
