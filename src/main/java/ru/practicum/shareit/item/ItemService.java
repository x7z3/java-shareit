package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto);

    void deleteItem(int itemId);

    ItemDto updateItem(ItemDto itemDto);

    ItemDto patchItem(ItemDto itemDto);

    ItemDto getItem(int itemId);

    List<ItemDto> searchItem(String searchText);

    List<ItemDto> getAllItems(Integer sharerUserId);
}
