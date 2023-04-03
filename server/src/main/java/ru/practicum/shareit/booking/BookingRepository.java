package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking createBooking(Booking booking);

    List<Booking> get(BookingState state, Integer userId, Integer from, Integer size);

    Booking get(Integer bookingId);

    List<Booking> getBookingsBy(Integer itemId, Integer userId);

    List<Booking> getBookingsByItems(BookingState state, List<Item> userId, Integer from, Integer size);

    List<Booking> getLastNextBooking(int item, Integer userId);
}
