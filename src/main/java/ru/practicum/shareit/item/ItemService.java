package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto);

    void deleteItem(int itemId);

    ItemDto updateItem(ItemDto itemDto);

    ItemDto patchItem(ItemDto itemDto);

    ItemDto getItem(Integer itemId, Integer sharerUserId);

    List<ItemDto> searchItem(String searchText);

    List<ItemDto> getAllItems(Integer sharerUserId);

    CommentDto addItemComment(CommentDto commentDto, Integer userId);
}
