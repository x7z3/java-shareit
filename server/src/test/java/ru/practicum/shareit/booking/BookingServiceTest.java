package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static ru.practicum.shareit.booking.BookingMapper.toBookingDto;

@SpringBootTest
class BookingServiceTest {
    private User itemUser;
    private Item item;
    private User requestUser;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Booking booking;
    private User user;
    private BookingDto bookingDto;

    @Autowired
    private BookingService bookingService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    @Qualifier("bookingRepository")
    private BookingRepository bookingRepository;

    @MockBean
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemUser = new User(2, "itemUser", "itemUser@mail.ru");
        item = new Item(1, "name", "description", true, itemUser, null);
        requestUser = new User(3, "requestUser", "requestUser@mail.ru");
        startTime = LocalDateTime.now().plus(Duration.ofHours(1));
        endTime = startTime.plus(Duration.ofHours(1));
        booking = new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING);
        user = new User(1, "user", "user1@mail.ru");
        bookingDto = BookingDto.builder().itemId(1).start(startTime).end(endTime).bookerId(1).build();
    }

    @Test
    void bookItem_whenBookWithCorrectUserAndItem_thenItemBooked() {
        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);
        Mockito.when(bookingRepository.createBooking(isA(Booking.class))).thenReturn(booking);

        BookingDto resultBookingDto = bookingService.bookItem(bookingDto, 1);

        assertEquals(resultBookingDto, toBookingDto(booking));
    }

    @Test
    void bookItem_whenItemIsNotAvailable_throwException() {
        Item item = new Item(1, "name", "description", false, itemUser, null);

        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        assertThrows(ItemNotAvailableException.class, () -> bookingService.bookItem(bookingDto, 1));
    }

    @Test
    void bookItem_whenUserIsItemOwner_throwException() {
        Item item = new Item(1, "name", "description", true, user, null);

        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        assertThrows(OwnerBookingException.class, () -> bookingService.bookItem(bookingDto, 1));
    }

    @Test
    void bookItem_whenStartTimeInThePast_throwException() {
        LocalDateTime startTime = LocalDateTime.now().minus(Duration.ofHours(1));

        BookingDto bookingDto = BookingDto.builder().itemId(1).start(startTime).end(endTime).bookerId(1).build();

        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        assertThrows(WrongBookingTimesException.class, () -> bookingService.bookItem(bookingDto, 1));
    }

    @Test
    void bookItem_whenStartTimeAndEndTimeAreEqual_throwException() {
        BookingDto bookingDto = BookingDto.builder().itemId(1).start(startTime).end(startTime).bookerId(1).build();

        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        assertThrows(WrongBookingTimesException.class, () -> bookingService.bookItem(bookingDto, 1));
    }

    @Test
    void bookItem_whenEndTimeInThePast_throwException() {
        LocalDateTime startTime = LocalDateTime.now().minus(Duration.ofHours(2));
        LocalDateTime endTime = startTime.plus(Duration.ofHours(1));

        BookingDto bookingDto = BookingDto.builder().itemId(1).start(startTime).end(endTime).bookerId(1).build();

        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        assertThrows(WrongBookingTimesException.class, () -> bookingService.bookItem(bookingDto, 1));
    }

    @Test
    void bookItem_whenEndTimeIsBeforeStartTime_throwException() {
        LocalDateTime endTime = startTime.minus(Duration.ofHours(1));

        BookingDto bookingDto = BookingDto.builder().itemId(1).start(startTime).end(endTime).bookerId(1).build();

        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        assertThrows(WrongBookingTimesException.class, () -> bookingService.bookItem(bookingDto, 1));
    }

    @Test
    void approveBooking_whenBookingStatusIsWaiting_thenBookingIsApproved() {
        Mockito.when(bookingRepository.get(anyInt())).thenReturn(booking);

        BookingDto bookingDto = bookingService.approveBooking(1, true, 2);

        assertEquals(BookingStatus.APPROVED, bookingDto.getStatus());
    }

    @Test
    void approveBooking_whenBookingStatusIsApproved_thenThrowException() {
        Booking booking = new Booking(1, startTime, endTime, item, requestUser, BookingStatus.APPROVED);
        Mockito.when(bookingRepository.get(anyInt())).thenReturn(booking);

        assertThrows(ShareItException.class, () -> bookingService.approveBooking(1, true, 2));
    }

    @Test
    void approveBooking_whenPassedUserIdIsNotEqualBookingItemOwnerId_thenThrowException() {
        Mockito.when(bookingRepository.get(anyInt())).thenReturn(booking);

        assertThrows(WrongItemOwnerException.class, () -> bookingService.approveBooking(1, true, 3));
    }

    @Test
    void getBookings_whenBookingsExists_thenBookingsSizeIsNotZero() {
        Mockito.when(bookingRepository.get(isA(BookingState.class), anyInt(), any(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> bookings = bookingService.getBookings(BookingState.ALL, 1, null, null);
        assertFalse(bookings.isEmpty());
    }

    @Test
    void getBookings_whenBookingsDontExist_thenThrowException() {
        Mockito.when(bookingRepository.get(isA(BookingState.class), anyInt(), any(), any()))
                .thenReturn(List.of());

        assertThrows(BookingsNotFoundException.class, () -> bookingService.getBookings(BookingState.ALL, 1, null, null));
    }

    @Test
    void getBooking_whenBookingIdAndUserIdAreCorrect_thenGetBooking() {
        Mockito.when(bookingRepository.get(anyInt())).thenReturn(booking);
        BookingDto resultBookingDto = bookingService.getBooking(1, 3);
        assertEquals(toBookingDto(booking), resultBookingDto);
    }

    @Test
    void getBooking_whenUserIdIsNotCorrect_thenThrowException() {
        Mockito.when(bookingRepository.get(anyInt())).thenReturn(booking);
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBooking(1, 1));
    }

    @Test
    void getOwnerBookings_whenGetOwnerBookingsWithCorrectParams_thenReturnListOfBookings() {
        Mockito.when(itemRepository.getAll(anyInt())).thenReturn(List.of(item));
        Mockito.when(bookingRepository.getBookingsByItems(isA(BookingState.class), anyList(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));
        List<BookingDto> ownerBookings = bookingService.getOwnerBookings(BookingState.ALL, 1, 1, 2);
        assertFalse(ownerBookings.isEmpty());
    }

    @Test
    void getOwnerBookings_whenBookingItemsNotFound_thenThrowException() {
        Mockito.when(itemRepository.getAll(anyInt())).thenReturn(List.of());
        Mockito.when(bookingRepository.getBookingsByItems(isA(BookingState.class), anyList(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));
        assertThrows(BookingsNotFoundException.class,
                () -> bookingService.getOwnerBookings(BookingState.ALL, 1, 1, 2));
    }

    @Test
    void getOwnerBookings_whenBookingsNotFound_thenThrowException() {
        Mockito.when(itemRepository.getAll(anyInt())).thenReturn(List.of(item));
        Mockito.when(bookingRepository.getBookingsByItems(isA(BookingState.class), anyList(), anyInt(), anyInt()))
                .thenReturn(List.of());
        assertThrows(BookingsNotFoundException.class,
                () -> bookingService.getOwnerBookings(BookingState.ALL, 1, 1, 2));
    }
}