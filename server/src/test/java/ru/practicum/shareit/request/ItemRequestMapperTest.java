package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemRequestMapperTest {

    @Test
    void toItemRequestDto() {
        ItemRequest itemRequest = new ItemRequest(1, "description", new User(), new Item(), LocalDateTime.now());
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest, List.of());

        assertNotNull(itemRequest);
        assertEquals(1, itemRequestDto.getId());
        assertEquals("description", itemRequestDto.getDescription());
    }

    @Test
    void toItemRequest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "description", 1, 1, LocalDateTime.now(), "itemName", List.of());
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, new User(), new Item());

        assertNotNull(itemRequest);
        assertEquals(1, itemRequest.getId());
        assertEquals("description", itemRequest.getDescription());
    }
}