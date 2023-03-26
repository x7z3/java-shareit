package ru.practicum.shareit.request;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Set;

public interface ItemRequestRepository {
    ItemRequest createItemRequest(ItemRequest itemRequest);

    List<ItemRequest> getItemRequests(User user);

    List<ItemRequest> getItemRequests(Integer from, Integer size, Set<Integer> itemRequestIds);

    ItemRequest getItemRequest(Integer itemRequestId);

}
