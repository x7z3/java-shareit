package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
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

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private User itemUser;
    private User requestUser;
    private Item item;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now().plus(Duration.ofHours(1));
        endTime = startTime.plus(Duration.ofHours(1));
    }

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
    void get_whenStateIsPast_thenSuccess() {
        LocalDateTime startTime = LocalDateTime.now().minus(Duration.ofHours(2));
        LocalDateTime endTime = startTime.plus(Duration.ofHours(1));
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED));
        List<Booking> bookings = bookingRepository.get(BookingState.PAST, requestUser.getId(), null, null);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenStateIsCurrent_thenSuccess() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plus(Duration.ofHours(1));
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED));
        List<Booking> bookings = bookingRepository.get(BookingState.CURRENT, requestUser.getId(), null, null);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenStateIsFuture_thenSuccess() {
        LocalDateTime startTime = LocalDateTime.now().plus(Duration.ofHours(1));
        LocalDateTime endTime = startTime.plus(Duration.ofHours(1));
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED));
        List<Booking> bookings = bookingRepository.get(BookingState.FUTURE, requestUser.getId(), null, null);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenStateIsRejected_thenSuccess() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.REJECTED));
        List<Booking> bookings = bookingRepository.get(BookingState.REJECTED, requestUser.getId(), null, null);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenStateIsWaiting_thenSuccess() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING));
        List<Booking> bookings = bookingRepository.get(BookingState.WAITING, requestUser.getId(), 0, 10);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenNoState_thenSuccess() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED));
        List<Booking> bookings = bookingRepository.get(BookingState.ALL, requestUser.getId(), 0, 10);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenNoStateNoPagination_thenSuccess() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED));
        List<Booking> bookings = bookingRepository.get(BookingState.ALL, requestUser.getId(), null, null);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void get_whenNoStatOnlyFrom_thenThrowNpe() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED));
        assertThrows(NullPointerException.class, () -> bookingRepository.get(BookingState.ALL, requestUser.getId(), 0, null));
    }

    @Test
    void get_whenNoStatOnlySize_thenThrowNpe() {
        bookingRepository.createBooking(new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED));
        assertThrows(NullPointerException.class, () -> bookingRepository.get(BookingState.ALL, requestUser.getId(), null, 10));
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