package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.exception.XSharerUserIdHeaderNotFoundException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto bookItem(
            @Valid @RequestBody BookingDto bookingDto,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        bookingDto.setStatus(BookingStatus.WAITING);
        return bookingService.bookItem(
                bookingDto, sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new)
        );
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeBookingStatus(
            @PathVariable Integer bookingId,
            @RequestParam boolean approved,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        return bookingService.approveBooking(
                bookingId, approved, sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new)
        );
    }

    @GetMapping
    public List<BookingDto> getBookings(
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        BookingState bookingState = getBookingState(state);
        return bookingService.getBookings(bookingState, sharerUserId.orElse(null), from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        BookingState bookingState = getBookingState(state);
        return bookingService.getOwnerBookings(bookingState, sharerUserId.orElse(null), from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @PathVariable Integer bookingId,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        return bookingService.getBooking(bookingId, sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new));
    }

    private BookingState getBookingState(String state) {
        BookingState[] values = BookingState.values();
        Optional<BookingState> bookingState = Arrays.stream(values).filter(b -> b.toString().equals(state)).findFirst();
        return bookingState.orElseThrow(() -> new ShareItException(
                String.format("Unknown state: %s", state)
        ));
    }
}
