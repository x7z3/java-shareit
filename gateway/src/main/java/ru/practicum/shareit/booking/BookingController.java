package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(
			@RequestHeader(ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size
	) {
		BookingState state = getBookingStateOrThrow(stateParam);
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(
			@Positive @RequestHeader(ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) long userId,
			@RequestBody @Valid BookItemRequestDto requestDto
	) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(
			@RequestHeader(ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) long userId,
			@Positive @PathVariable Long bookingId
	) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}


	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> changeBookingStatus(
			@Positive @PathVariable Integer bookingId,
			@RequestParam boolean approved,
			@Positive @RequestHeader(ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) long userId
	) {
		log.info("Patch booking {}, approved {}, userId {}", bookingId, approved, userId);
		return bookingClient.patchBooking(bookingId, approved, userId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Positive @RequestParam(defaultValue = "10") Integer size,
			@RequestParam(required = false, defaultValue = "ALL") String state,
			@Positive @RequestHeader(ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) long userId
	) {
		log.info(
				"Get owner bookings from {}, size {}, state {}, userId {}",
				from,
				size,
				state,
				userId
		);
		BookingState bookingState = getBookingStateOrThrow(state);
		return bookingClient.getOwnerBookings(from, size, bookingState, userId);
	}

	private static BookingState getBookingStateOrThrow(String stateParam) {
		return BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
	}
}
