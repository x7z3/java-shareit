package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl, API_PREFIX);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> patchBooking(Integer bookingId, boolean approved, long userId) {
        Map<String, Object> params = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        return patch("/{bookingId}?approved={approved}", userId, params, null);
    }

    public ResponseEntity<Object> getOwnerBookings(Integer from, Integer size, BookingState state, long userId) {
        Map<String, Object> params = Map.of(
                "from", from,
                "size", size,
                "state", state
        );
        return get("/owner?from={from}&size={size}&state={state}", userId, params);
    }
}
