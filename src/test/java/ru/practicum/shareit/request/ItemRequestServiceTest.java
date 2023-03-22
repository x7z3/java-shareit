package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

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

    private final User requestor = new User(1, "requestor", "requestor@mail.ru");
    private final User itemUser = new User(2, "itemUser", "itemUser@mail.ru");
    private final User requestUser = new User(3, "requestUser", "requestUser@mail.ru");
    private final Item item = new Item(1, "name", "description", true, itemUser, null);
    private final ItemDto itemDto = toItemDto(item);
    private final ItemRequest itemRequest = new ItemRequest(1, "description", requestUser, item, LocalDateTime.now());
    private final LocalDateTime startTime = LocalDateTime.now().plus(Duration.ofHours(1));
    private final LocalDateTime endTime = startTime.plus(Duration.ofHours(1));
    private final Booking booking = new Booking(1, startTime, endTime, item, requestUser, BookingStatus.WAITING);
    private final User user = new User(1, "user", "user1@mail.ru");
    private final BookingDto bookingDto = BookingDto.builder().itemId(1).start(startTime).end(endTime).bookerId(1).build();

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