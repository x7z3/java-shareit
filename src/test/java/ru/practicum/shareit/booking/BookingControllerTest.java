package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = BookingController.class
)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    private final LocalDateTime now = LocalDateTime.now();

    private final BookingDto bookingDto = BookingDto.builder()
            .itemId(2)
            .start(now)
            .end(now.plus(Duration.ofSeconds(4)))
            .build();

    @Test
    void bookItem() throws Exception {
        Mockito.when(
                bookingService.bookItem(isA(BookingDto.class), isA(Integer.class))
        ).thenReturn(bookingDto);

        mockMvc.perform(
                post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 2)
        ).andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1))
                .bookItem(isA(BookingDto.class), anyInt());
    }

    @Test
    void changeBookingStatus() throws Exception {
        Mockito.when(
                bookingService.approveBooking(1, true, 2)
        ).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1")
                .queryParam("approved", "true")
                .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 2)
        ).andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1))
                .approveBooking(anyInt(), anyBoolean(), anyInt());
    }

    @Test
    void getBookings() throws Exception {
        Mockito.when(
                bookingService.getBookings(isA(BookingState.class), anyInt(), anyInt(), anyInt())
        ).thenReturn(List.of(bookingDto));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings")
                        .queryParam("from", "1")
                        .queryParam("size", "1")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 2)
        ).andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1))
                .getBookings(isA(BookingState.class), anyInt(), anyInt(), anyInt());
    }

    @Test
    void getOwnerBookings() throws Exception {
        Mockito.when(
                bookingService.getOwnerBookings(isA(BookingState.class), anyInt(), anyInt(), anyInt())
        ).thenReturn(List.of(bookingDto));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings/owner")
                        .queryParam("from", "1")
                        .queryParam("size", "1")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 2)
        ).andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1))
                .getOwnerBookings(isA(BookingState.class), anyInt(), anyInt(), anyInt());
    }

    @Test
    void getBooking() throws Exception {
        Mockito.when(
                bookingService.getBooking(anyInt(), anyInt())
        ).thenReturn(bookingDto);

        mockMvc.perform(
                get("/bookings/1")
                        .header(ShareItApp.X_SHARER_USER_ID_HEADER_NAME, 2)

        ).andExpect(status().isOk());

        Mockito.verify(bookingService, Mockito.times(1)).getBooking(anyInt(), anyInt());
    }
}