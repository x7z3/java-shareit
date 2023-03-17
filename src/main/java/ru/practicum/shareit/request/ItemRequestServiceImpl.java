package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto) {
        if (itemRequestDto.getRequestorId() == null || itemRequestDto.getDescription() == null)
            throw new ShareItException("Invalid data have received");
        User requestor = userRepository.getUser(itemRequestDto.getRequestorId());

        Item item = null;
        if (itemRequestDto.getItemId() != null) item = itemRepository.get(itemRequestDto.getItemId());

        if (itemRequestDto.getCreated() == null) itemRequestDto.setCreated(LocalDateTime.now());

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, requestor, item);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.createItemRequest(itemRequest), new ArrayList<>());
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Integer userId) {
        User requestor = userRepository.getUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.getItemRequests(requestor);
        return findItemRequestsItems(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Integer from, Integer size, Integer userId) {
        throwIfPaginationParamsIncorrect(from, size);

        Set<Integer> itemRequestIds = itemRepository.getAll(userId)
                .stream()
                .filter(item -> item.getItemRequest() != null)
                .map(item -> item.getItemRequest().getId())
                .collect(Collectors.toSet());

        List<ItemRequest> itemRequests = itemRequestRepository.getItemRequests(from, size, itemRequestIds);
        return findItemRequestsItems(itemRequests);
    }

    private void throwIfPaginationParamsIncorrect(Integer from, Integer size) {
        if (from != null && size != null &&  (from < 0 || size < 0 || size == 0))
            throw new ShareItException("Wrong pagination parameters was sent");
    }

    @Override
    public ItemRequestDto getItemRequest(Integer itemRequestId, Integer userId) {
        userRepository.getUser(userId);
        ItemRequest itemRequest = itemRequestRepository.getItemRequest(itemRequestId);
        return getItemRequestDto(itemRequest);
    }

    private List<ItemRequestDto> findItemRequestsItems(List<ItemRequest> itemRequests) {
        return itemRequests.stream().map(this::getItemRequestDto).collect(Collectors.toList());
    }

    private ItemRequestDto getItemRequestDto(ItemRequest itemRequest) {
        List<ItemDto> requestItems = itemRepository.getItemByRequest(itemRequest)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        return ItemRequestMapper.toItemRequestDto(itemRequest, requestItems);
    }
}
