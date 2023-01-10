package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository {
    Item create(Item item);

    void delete(int itemId);

    Item update(Item item);

    Item get(int itemId);

    List<Item> search(String searchText);

    Item patch(Item item);

    List<Item> getAll(Integer sharerUserId);
}
