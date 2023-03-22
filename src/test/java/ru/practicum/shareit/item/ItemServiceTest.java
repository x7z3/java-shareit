package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static ru.practicum.shareit.comment.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@SpringBootTest
class ItemServiceTest {
    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    @Qualifier("bookingRepository")
    private BookingRepository bookingRepository;
    @MockBean
    private ItemRequestRepository itemRequestRepository;

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
    void createItem() {
        Mockito.when(itemRepository.create(isA(Item.class))).thenReturn(item);
        Mockito.when(itemRequestRepository.getItemRequest(anyInt())).thenReturn(itemRequest);

        itemDto.setRequestId(1);

        ItemDto resultItemDto = itemService.createItem(itemDto);
        assertNotNull(resultItemDto);

        Mockito.verify(itemRepository, Mockito.times(1)).create(isA(Item.class));
        Mockito.verify(itemRequestRepository, Mockito.times(1)).getItemRequest(anyInt());
    }

    @Test
    void deleteItem() {
        Mockito.doAnswer((Answer<Void>) invocation -> null).when(itemRepository).delete(anyInt());
        itemService.deleteItem(1);
        Mockito.verify(itemRepository, Mockito.times(1)).delete(1);
    }

    @Test
    void updateItem() {
        Mockito.when(itemRepository.update(isA(Item.class))).thenReturn(item);
        Mockito.when(itemRequestRepository.getItemRequest(anyInt())).thenReturn(itemRequest);

        itemDto.setRequestId(1);
        ItemDto resultItemDto = itemService.updateItem(itemDto);

        assertNotNull(resultItemDto);

        Mockito.verify(itemRepository, Mockito.times(1)).update(any(Item.class));
        Mockito.verify(itemRequestRepository, Mockito.times(1)).getItemRequest(anyInt());
    }

    @Test
    void patchItem() {
        Mockito.when(itemRepository.patch(isA(Item.class))).thenReturn(item);
        Mockito.when(itemRequestRepository.getItemRequest(anyInt())).thenReturn(itemRequest);

        itemDto.setRequestId(1);
        ItemDto resultItemDto = itemService.patchItem(itemDto);

        assertNotNull(resultItemDto);

        Mockito.verify(itemRepository, Mockito.times(1)).patch(any(Item.class));
        Mockito.verify(itemRequestRepository, Mockito.times(1)).getItemRequest(anyInt());
    }

    @Test
    void getAllItems() {
        Mockito.when(itemRepository.getAll(anyInt())).thenReturn(List.of(item));
        Mockito.when(bookingRepository.getLastNextBooking(anyInt(), anyInt())).thenReturn(List.of(booking, booking));
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        Comment comment = new Comment(1, "comment text", item, user, LocalDateTime.now());
        Mockito.when(commentRepository.getItemComments(anyInt())).thenReturn(List.of(comment));

        List<ItemDto> allItems = itemService.getAllItems(1);

        assertNotNull(allItems);
        assertFalse(allItems.isEmpty());
        Mockito.verify(itemRepository, Mockito.times(1)).getAll(1);
        Mockito.verify(bookingRepository, Mockito.times(1)).getLastNextBooking(1, 1);
        Mockito.verify(itemRepository, Mockito.times(1)).get(1);
        Mockito.verify(commentRepository, Mockito.times(1)).getItemComments(1);
    }

    @Test
    void getItem() {
        Mockito.when(bookingRepository.getLastNextBooking(anyInt(), anyInt())).thenReturn(List.of(booking, booking));
        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);

        Comment comment = new Comment(1, "comment text", item, user, LocalDateTime.now());
        Mockito.when(commentRepository.getItemComments(anyInt())).thenReturn(List.of(comment));

        ItemDto resultItem = itemService.getItem(1, 1);

        assertNotNull(resultItem);
        Mockito.verify(bookingRepository, Mockito.times(1)).getLastNextBooking(1, 1);
        Mockito.verify(itemRepository, Mockito.times(1)).get(1);
        Mockito.verify(commentRepository, Mockito.times(1)).getItemComments(1);
    }

    @Test
    void searchItem() {
        Mockito.when(itemRepository.search(anyString())).thenReturn(List.of(item));
        List<ItemDto> itemDtos = itemService.searchItem("search text");

        assertNotNull(itemDtos);
        assertFalse(itemDtos.isEmpty());

        Mockito.verify(itemRepository, Mockito.times(1)).search("search text");
    }

    @Test
    void addItemComment() {
        Comment comment = new Comment(1, "comment text", item, user, LocalDateTime.now());
        CommentDto commentDto = toCommentDto(comment);

        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);
        Mockito.when(bookingRepository.getBookingsByItems(isA(BookingState.class), anyList(), isNull(), isNull()))
                .thenReturn(List.of(booking));
        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);
        Mockito.when(commentRepository.addComment(isA(Comment.class))).thenReturn(comment);
        Mockito.when(bookingRepository.getBookingsBy(anyInt(), anyInt())).thenReturn(List.of(booking));

        CommentDto resultCommentDto = itemService.addItemComment(commentDto, 1);

        assertNotNull(resultCommentDto);

        Mockito.verify(itemRepository, Mockito.times(1)).get(anyInt());
        Mockito.verify(bookingRepository, Mockito.times(1))
                .getBookingsByItems(isA(BookingState.class), anyList(), isNull(), isNull());
        Mockito.verify(userRepository, Mockito.times(1)).getUser(anyInt());
        Mockito.verify(commentRepository, Mockito.times(1)).addComment(isA(Comment.class));
    }

    @Test
    void addItemComment_whenZeroBookings_thenThrowException() {
        Comment comment = new Comment(1, "comment text", item, user, LocalDateTime.now());
        CommentDto commentDto = toCommentDto(comment);

        Mockito.when(itemRepository.get(anyInt())).thenReturn(item);
        Mockito.when(bookingRepository.getBookingsByItems(isA(BookingState.class), anyList(), isNull(), isNull()))
                .thenReturn(List.of());

        assertThrows(ShareItException.class, () -> itemService.addItemComment(commentDto, 1));
    }
}