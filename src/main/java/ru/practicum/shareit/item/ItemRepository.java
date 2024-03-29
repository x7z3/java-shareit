package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    void delete(int itemId);

    Item update(Item item);

    Item get(int itemId);

    List<Item> search(String searchText);

    Item patch(Item item);

    List<Item> getAll(Integer sharerUserId);

    List<Item> getItemByRequest(ItemRequest itemRequest);
}
