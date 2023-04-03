package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ComponentScan("ru.practicum.shareit")
class ItemRequestRepositoryTest {
    private ItemRequestRepository itemRequestRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    private User requestor;
    private User itemOwner;
    private ItemRequest itemRequest;
    private Item item;

    @Autowired
    public ItemRequestRepositoryTest(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;

        requestor = userRepository.create(new User(1, "requestor", "requestor@mail.ru"));
        itemOwner = userRepository.create(new User(2, "itemOwner", "itemOwner@mail.ru"));
        item = itemRepository.create(new Item(1, "name", "description", true, itemOwner, null));
        itemRequest = itemRequestRepository.createItemRequest(new ItemRequest(1, "description", requestor, item, LocalDateTime.now()));
    }

    @Test
    void createItemRequest() {
        ItemRequest resultItemRequest = itemRequestRepository.createItemRequest(itemRequest);
        assertNotNull(resultItemRequest);
    }

    @Test
    void getItemRequests() {
        itemRequestRepository.createItemRequest(itemRequest);
        List<ItemRequest> itemRequests = itemRequestRepository.getItemRequests(requestor);
        assertNotNull(itemRequests);
        assertFalse(itemRequests.isEmpty());
    }

    @Test
    void testGetItemRequests() {
        List<ItemRequest> itemRequests = itemRequestRepository.getItemRequests(1, 10, Set.of(1));
        assertNotNull(itemRequests);
        assertFalse(itemRequests.isEmpty());
    }

    @Test
    void getItemRequest() {
        ItemRequest resultItemRequest = itemRequestRepository.getItemRequest(1);
        assertNotNull(resultItemRequest);
    }
}