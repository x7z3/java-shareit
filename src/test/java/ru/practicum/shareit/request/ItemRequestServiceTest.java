package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class ItemRequestServiceTest {
    @Autowired
    public ItemRequestService itemRequestService;

    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ItemRequestRepository itemRequestRepository;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1)
            .itemId(1)
            .itemName("item name")
            .description("description")
            .requestorId(1)
            .created(LocalDateTime.now())
            .build();

    private User requestor = new User(1, "requestor", "requestor@mail.ru");
    private User itemUser = new User(2, "itemUser", "itemUser@mail.ru");
    private User requestUser = new User(3, "requestUser", "requestUser@mail.ru");
    private Item item = new Item(1, "name", "description", true, itemUser, null);
    private ItemRequest itemRequest = new ItemRequest(1, "description", requestUser, item, LocalDateTime.now());
    private LocalDateTime startTime = LocalDateTime.now().plus(Duration.ofHours(1));
    private LocalDateTime endTime = startTime.plus(Duration.ofHours(1));
    private User user = new User(1, "user", "user1@mail.ru");

    @BeforeEach
    void setUp() {
        requestor = new User(1, "requestor", "requestor@mail.ru");
        itemUser = new User(2, "itemUser", "itemUser@mail.ru");
        requestUser = new User(3, "requestUser", "requestUser@mail.ru");
        item = new Item(1, "name", "description", true, itemUser, null);
        itemRequest = new ItemRequest(1, "description", requestUser, item, LocalDateTime.now());
        startTime = LocalDateTime.now().plus(Duration.ofHours(1));
        endTime = startTime.plus(Duration.ofHours(1));
        user = new User(1, "user", "user1@mail.ru");
    }

    @Test
    void createItemRequest() {
        Mockito.when(userRepository.getUser(anyInt())).thenReturn(requestor);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);
        Mockito.when(itemRequestRepository.createItemRequest(isA(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto resultItemRequest = itemRequestService.createItemRequest(itemRequestDto);
        assertNotNull(resultItemRequest);

        Mockito.verify(userRepository, Mockito.times(1)).getUser(anyInt());
        Mockito.verify(itemRepository, Mockito.times(1)).get(anyInt());
        Mockito.verify(itemRequestRepository, Mockito.times(1)).createItemRequest(isA(ItemRequest.class));
    }

    @Test
    void createItemRequest_whenCreatedNullAndItemIdNotNull_thenSuccess() {
        Mockito.when(userRepository.getUser(anyInt())).thenReturn(requestor);
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);
        Mockito.when(itemRequestRepository.createItemRequest(isA(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .itemId(1)
                .itemName("item name")
                .description("description")
                .requestorId(1)
                .build();

        ItemRequestDto resultItemRequest = itemRequestService.createItemRequest(itemRequestDto);
        assertNotNull(resultItemRequest);

        Mockito.verify(userRepository, Mockito.times(1)).getUser(anyInt());
        Mockito.verify(itemRepository, Mockito.times(1)).get(anyInt());
        Mockito.verify(itemRequestRepository, Mockito.times(1)).createItemRequest(isA(ItemRequest.class));
    }

    @Test
    void createItemRequest_throwIfRequestorIdIsNull() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1)
                .itemId(1)
                .itemName("item name")
                .description("description")
                .created(LocalDateTime.now())
                .build();

        assertThrows(ShareItException.class, () -> itemRequestService.createItemRequest(itemRequestDto));
    }

    @Test
    void getItemRequests() {
        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRequestRepository.getItemRequests(isA(User.class))).thenReturn(List.of(itemRequest));
        Mockito.when(itemRepository.getItemByRequest(isA(ItemRequest.class))).thenReturn(List.of(item));

        List<ItemRequestDto> itemRequests = itemRequestService.getItemRequests(1);
        assertNotNull(itemRequests);
        assertFalse(itemRequests.isEmpty());

        Mockito.verify(userRepository, Mockito.times(1)).getUser(anyInt());
        Mockito.verify(itemRequestRepository, Mockito.times(1)).getItemRequests(isA(User.class));
        Mockito.verify(itemRepository, Mockito.times(1)).getItemByRequest(isA(ItemRequest.class));
    }

    @Test
    void getItemRequests_whenFromAndSizeSpecified_thenReturnItemRequests() {
        Mockito.when(itemRepository.getAll(anyInt())).thenReturn(List.of(item));
        Mockito.when(itemRequestRepository.getItemRequests(anyInt(), anyInt(), anySet())).thenReturn(List.of(itemRequest));
        Mockito.when(itemRepository.getItemByRequest(isA(ItemRequest.class))).thenReturn(List.of(item));

        List<ItemRequestDto> itemRequests = itemRequestService.getItemRequests(1, 10, 1);
        assertNotNull(itemRequests);
        assertFalse(itemRequests.isEmpty());

        Mockito.verify(itemRepository, Mockito.times(1)).getAll(anyInt());
        Mockito.verify(itemRequestRepository, Mockito.times(1)).getItemRequests(anyInt(), anyInt(), anySet());
        Mockito.verify(itemRepository, Mockito.times(1)).getItemByRequest(isA(ItemRequest.class));
    }

    @Test
    void getItemRequests_whenWrongSizeAndFromParams_thenThrowException() {
        assertThrows(ShareItException.class, () -> itemRequestService.getItemRequests(-1, 10, 1));
        assertThrows(ShareItException.class, () -> itemRequestService.getItemRequests(1, -10, 1));
        assertThrows(ShareItException.class, () -> itemRequestService.getItemRequests(1, 0, 1));
    }

    @Test
    void getItemRequest() {
        Mockito.when(itemRepository.getItemByRequest(isA(ItemRequest.class))).thenReturn(List.of(item));
        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(itemRequestRepository.getItemRequest(anyInt())).thenReturn(itemRequest);

        ItemRequestDto resultItemRequest = itemRequestService.getItemRequest(1, 1);
        assertNotNull(resultItemRequest);

        Mockito.verify(userRepository, Mockito.times(1)).getUser(anyInt());
        Mockito.verify(itemRequestRepository, Mockito.times(1)).getItemRequest(anyInt());
        Mockito.verify(itemRepository, Mockito.times(1)).getItemByRequest(isA(ItemRequest.class));
    }
}