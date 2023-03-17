package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getItemRequests(Integer userId);

    List<ItemRequestDto> getItemRequests(Integer from, Integer size, Integer userId);

    ItemRequestDto getItemRequest(Integer itemRequestId, Integer userId);
}
