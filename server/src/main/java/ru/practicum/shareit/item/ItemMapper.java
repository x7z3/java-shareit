package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static ItemDto toItemDto(
            Item item, List<CommentDto> commentDtos,
            BookingDto lastBooking,
            BookingDto nextBooking
    ) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? item.getOwner().getId() : null,
                item.getItemRequest() != null ? item.getItemRequest().getId() : null,
                commentDtos,
                lastBooking,
                nextBooking
        );
    }

    public static ItemDto toItemDto(Item item, List<CommentDto> commentDtos) {
        return toItemDto(item, commentDtos, null, null);
    }

    public static ItemDto toItemDto(Item item) {
        return toItemDto(item, new ArrayList<>(), null, null);
    }

    public static Item toItem(ItemDto itemDto, User owner, ItemRequest itemRequest) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                owner,
                itemRequest
        );
    }
}
