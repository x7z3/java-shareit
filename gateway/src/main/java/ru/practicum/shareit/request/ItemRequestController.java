package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(
            @RequestBody @Valid ItemRequestDto itemRequestDto,
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemRequestClient.getItemRequests(
                userId
        );
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemRequestClient.getItemRequests(
                from,
                size,
                userId
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequest(
            @Positive @PathVariable("id") Integer itemRequestId,
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemRequestClient.getItemRequest(
                itemRequestId,
                userId
        );
    }
}
