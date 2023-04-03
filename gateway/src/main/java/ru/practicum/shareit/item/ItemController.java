package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(
            @RequestBody @Valid ItemDto itemDto,
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemClient.createItem(userId, itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(
            @Positive @PathVariable("id") int itemId
    ) {
        itemClient.deleteItem(itemId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchItem(
            @Positive @PathVariable("id") int id,
            @RequestBody ItemDto itemDto,
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {

        return itemClient.patchItem(id, itemDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(
            @Positive @PathVariable("id") int itemId,
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(
            @NotBlank @RequestParam("text") String searchText
    ) {
        return itemClient.searchItem(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addItemComment(
            @Positive @PathVariable Integer itemId,
            @RequestBody @Valid CommentDto commentDto,
            @Positive @RequestHeader(value = ShareItGateway.X_SHARER_USER_ID_HEADER_NAME) Long userId
    ) {
        return itemClient.addItemComment(itemId, commentDto, userId);
    }
}
