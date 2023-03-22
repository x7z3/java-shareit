package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan("ru.practicum.shareit")
class BookingRepositoryTest {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final LocalDateTime startTime = LocalDateTime.now().plus(Duration.ofHours(1));
    private final LocalDateTime endTime = startTime.plus(Duration.ofHours(1));
    private User itemUser;
    private User requestUser;
    private Item item;

    @Autowired
    public BookingRepositoryTest(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;

        itemUser = userRepository.create(new User(1, "itemUser", "itemUser@mail.ru"));
        requestUser = userRepository.create(new User(2, "requestUser", "requestUser@mail.ru"));
        item = itemRepository.create(new Item(1, "name", "description", true, itemUser, null));
    }

    @Test
    void createBooking_whenSaveCorrectBooking_thenBookingSaved() {
        Booking booking = bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING));
        Booking savedBooking = bookingRepository.save(booking);
        assertNotNull(savedBooking);
        assertEquals(booking, savedBooking);
    }

    @Test
    void getBookingsByItems_whenSizeAndFromNotNull_thenReturnListOfBookings() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING));
        List<Booking> bookings = bookingRepository.getBookingsByItems(BookingState.ALL, List.of(item), 1, 20);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookingsByItems_whenSizeAndFromNull_thenReturnListOfBookings() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING));
        List<Booking> bookings = bookingRepository.getBookingsByItems(BookingState.ALL, List.of(item), null, null);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenGetBookingById_thenBookingIsReturned() {
        Booking booking = bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING));
        Booking returnedBooking = bookingRepository.get(booking.getId());
        assertNotNull(returnedBooking);
        assertEquals(booking, returnedBooking);
    }

    @Test
    void getBookingsBy_whenUserIdAndItemIdSpecified_thenReturnListOfBookings() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING));
        List<Booking> bookingsBy = bookingRepository.getBookingsBy(item.getId(), requestUser.getId());
        assertNotNull(bookingsBy);
        assertFalse(bookingsBy.isEmpty());
    }

    @Test
    void getLastNextBooking_whenItemIdAndUserIdAreCorrect_thenReturnListOfLastAndNextBookings() {
        List<Booking> lastNextBooking = bookingRepository.getLastNextBooking(item.getId(), requestUser.getId());
        assertNotNull(lastNextBooking);
        assertFalse(lastNextBooking.isEmpty());
    }
}