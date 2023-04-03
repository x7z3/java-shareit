package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentMapper;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto) {
        User owner = findOwner(itemDto);
        ItemRequest itemRequest = getItemRequest(itemDto);
        return toItemDto(itemRepository.create(toItem(itemDto, owner, itemRequest)));
    }

    @Override
    @Transactional
    public void deleteItem(int itemId) {
        itemRepository.delete(itemId);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto) {
        User owner = findOwner(itemDto);
        ItemRequest itemRequest = getItemRequest(itemDto);
        return toItemDto(itemRepository.update(toItem(itemDto, owner, itemRequest)));
    }

    @Override
    @Transactional
    public ItemDto patchItem(ItemDto itemDto) {
        User owner = findOwner(itemDto);
        ItemRequest itemRequest = getItemRequest(itemDto);
        return toItemDto(itemRepository.patch(toItem(itemDto, owner, itemRequest)));
    }

    @Override
    public List<ItemDto> getAllItems(Integer userId) {
        List<Item> items = itemRepository.getAll(userId);
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(
                    getItem(item.getId(), userId)
            );
        }
        return itemDtos;
    }

    @Override
    public ItemDto getItem(Integer itemId, Integer userId) {
        List<Booking> lastNextBookings = bookingRepository.getLastNextBooking(itemId, userId);
        List<CommentDto> commentDtos = commentRepository.getItemComments(itemId)
                .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
        return toItemDto(itemRepository.get(itemId),
                commentDtos,
                BookingMapper.toBookingDto(lastNextBookings.get(0)),
                BookingMapper.toBookingDto(lastNextBookings.get(1))
        );
    }

    @Override
    public List<ItemDto> searchItem(String searchText) {
        return itemRepository.search(searchText).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addItemComment(CommentDto commentDto, Integer userId) {
        Integer itemId = commentDto.getItemId();
        throwIfUserDidntBookItem(itemId, userId);

        Item item = itemRepository.get(itemId);
        List<Booking> itemBookings = bookingRepository.getBookingsByItems(BookingState.PAST, List.of(item), null, null);

        if (itemBookings.isEmpty())
            throw new ShareItException("No bookings for selected item");

        User user = userRepository.getUser(userId);
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        Comment createdComment = commentRepository.addComment(comment);
        return CommentMapper.toCommentDto(createdComment);
    }

    private ItemRequest getItemRequest(ItemDto itemDto) {
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null)
            itemRequest = itemRequestRepository.getItemRequest(itemDto.getRequestId());
        return itemRequest;
    }

    private void throwIfUserDidntBookItem(Integer itemId, Integer userId) {
        List<Booking> userItemBookings = bookingRepository.getBookingsBy(itemId, userId);
        if (userItemBookings.isEmpty()) throw new BookingNotFoundException(
                String.format("User's (ID=%s) booking of item with ID=%s was not found ", userId, itemId)
        );
    }

    private User findOwner(ItemDto itemDto) {
        Integer ownerId = itemDto.getOwnerId();
        User owner = null;
        if (ownerId != null) {
            owner = userRepository.getUser(ownerId);
        }
        return owner;
    }
}
