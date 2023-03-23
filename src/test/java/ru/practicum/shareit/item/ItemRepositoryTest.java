package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan("ru.practicum.shareit")
class ItemRepositoryTest {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final LocalDateTime startTime = LocalDateTime.now().plus(Duration.ofHours(1));
    private final LocalDateTime endTime = startTime.plus(Duration.ofHours(1));

    private User itemUser;
    private User requestUser;
    private User itemRequestor;
    private Item item;
    private ItemRequest itemRequest;

    @Autowired
    public ItemRepositoryTest(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemRequestRepository = itemRequestRepository;

        itemUser = userRepository.create(new User(1, "itemUser", "itemUser@mail.ru"));
        requestUser = userRepository.create(new User(2, "requestUser", "requestUser@mail.ru"));
        itemRequestor = userRepository.create(new User(2, "itemRequestor", "itemRequestor@mail.ru"));
        item = itemRepository.create(new Item(1, "name", "description", true, itemUser, null));
        itemRequest = itemRequestRepository.createItemRequest(
                new ItemRequest(1, "description", itemRequestor, item, LocalDateTime.now())
        );
    }

    @Test
    void create() {
        Item resultItem = itemRepository.create(item);
        assertNotNull(resultItem);
    }

    @Test
    void delete() {
        Item deletingItem = itemRepository.create(new Item(1, "name", "description", true, itemUser, null));
        assertDoesNotThrow(() -> itemRepository.delete(deletingItem.getId()));
    }

    @Test
    void update() {
        Item update = itemRepository.update(item);
        assertNotNull(update);
        assertEquals(item, update);
    }

    @Test
    void get() {
        Item resultItem = itemRepository.get(item.getId());
        assertNotNull(resultItem);
    }

    @Test
    void search() {
        List<Item> name = itemRepository.search("name");
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }

    @Test
    void patch() {
        Item resultItem = itemRepository.patch(item);
        assertNotNull(resultItem);
    }

    @Test
    void getAll() {
        List<Item> all = itemRepository.getAll(1);
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    void getItemByRequest() {
        item.setItemRequest(itemRequest);
        itemRepository.update(item);
        List<Item> resultList = itemRepository.getItemByRequest(itemRequest);
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
    }
}