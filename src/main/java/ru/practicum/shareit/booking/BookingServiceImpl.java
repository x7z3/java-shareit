package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.BookingMapper.toBookingDto;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto bookItem(BookingDto bookingDto, Integer userId) {
        User user = userRepository.getUser(userId);
        Item item = itemRepository.get(bookingDto.getItemId());

        throwIfItemIsNotAvailable(item);
        throwIfUserIsOwnerOfItem(userId, item);

        Booking booking = toBooking(bookingDto, item, user);
        throwIfDatesAreWrong(booking);

        Booking resultBooking = bookingRepository.createBooking(booking);
        return toBookingDto(resultBooking);
    }

    private void throwIfUserIsOwnerOfItem(Integer userId, Item item) {
        if (userId.equals(item.getOwner().getId())) throw new OwnerBookingException();
    }

    private void throwIfItemIsNotAvailable(Item item) {
        if (!item.getAvailable()) throw new ItemNotAvailableException();
    }

    private void throwIfDatesAreWrong(Booking booking) {
        LocalDateTime startTime = booking.getStartTime();
        LocalDateTime endTime = booking.getEndTime();

        LocalDateTime now = LocalDateTime.now();

        if (endTime.isBefore(startTime)) throw new WrongBookingTimesException("End time is before start time");
        if (startTime.isEqual(endTime)) throw new WrongBookingTimesException("Start time and End time are equal");
        if (endTime.isBefore(now)) throw new WrongBookingTimesException("End time is in the past");
        if (startTime.isBefore(now)) throw new WrongBookingTimesException("Start time is in the past");
    }


    @Override
    @Transactional
    public BookingDto approveBooking(Integer bookingId, boolean approved, Integer userId) {
        Booking booking = bookingRepository.get(bookingId);
        BookingStatus bookingStatus = booking.getStatus();
        if (BookingStatus.APPROVED.equals(bookingStatus)) {
            throw new ShareItException("No bookings to approve");
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) throw new WrongItemOwnerException(booking.getItem(), userId);
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookings(BookingState state, Integer userId, Integer from, Integer size) {
        throwIfPaginationParamsIncorrect(from, size);

        List<Booking> bookings = bookingRepository.get(state, userId, from, size);
        if (bookings.isEmpty()) throw new BookingsNotFoundException(state, userId);
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(BookingState state, Integer userId, Integer from, Integer size) {
        throwIfPaginationParamsIncorrect(from, size);

        List<Item> ownerItems = itemRepository.getAll(userId);
        if (ownerItems.isEmpty()) throw new BookingsNotFoundException(state, userId);

        List<Booking> bookings = bookingRepository.getBookingsByItems(state, ownerItems, from, size);
        if (bookings.isEmpty()) throw new BookingsNotFoundException(state, userId);
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto getBooking(Integer bookingId, Integer userId) {
        Booking booking = bookingRepository.get(bookingId);
        throwIfUserIsNotOwnerOrBooker(booking, userId);

        return BookingMapper.toBookingDto(booking);
    }

    private void throwIfUserIsNotOwnerOrBooker(Booking booking, Integer userId) {
        Integer bookerId = booking.getBooker().getId();
        Integer ownerId = booking.getItem().getOwner().getId();
        if (!userId.equals(bookerId) && !userId.equals(ownerId)) throw new BookingNotFoundException(
                "No bookings found for current user"
        );
    }

    private void throwIfPaginationParamsIncorrect(Integer from, Integer size) {
        if (from != null && size != null &&  (from < 0 || size < 0 || size == 0))
            throw new ShareItException("Wrong pagination parameters was sent");
    }
}
