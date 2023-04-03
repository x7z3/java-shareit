package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.validation.NotNullFuture;
import ru.practicum.shareit.booking.validation.NotNullFutureOrPresent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    private long itemId;
    @NotNullFutureOrPresent
    private LocalDateTime start;
    @NotNullFuture
    private LocalDateTime end;
    private Integer id;
    private Integer bookerId;
    private BookingState status;
    private ItemDto item;
    private UserDto booker;
}
