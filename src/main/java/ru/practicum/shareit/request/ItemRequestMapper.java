package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> requestItems) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor().getId(),
                itemRequest.getItem() != null ? itemRequest.getItem().getId() : null,
                itemRequest.getCreated(),
                itemRequest.getItem() != null ? itemRequest.getItem().getName() : null,
                requestItems
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor, Item item) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                requestor,
                item,
                itemRequestDto.getCreated()
        );
    }
}
