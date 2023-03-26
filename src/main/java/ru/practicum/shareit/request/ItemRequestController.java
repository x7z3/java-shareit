package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.exception.XSharerUserIdHeaderNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(
            @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        itemRequestDto.setRequestorId(sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new));
        return itemRequestService.createItemRequest(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequests(
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        return itemRequestService.getItemRequests(
                sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new)
        );
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequests(
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        return itemRequestService.getItemRequests(
                from,
                size,
                sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new)
        );
    }

    @GetMapping("/{id}")
    public ItemRequestDto getItemRequest(
            @PathVariable("id") Integer itemRequestId,
            @RequestHeader(value = ShareItApp.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        return itemRequestService.getItemRequest(
                itemRequestId,
                sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new)
        );
    }
}
