package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.XSharerUserIdHeaderNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader(value = ShareItServer.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        itemDto.setOwnerId(sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new));
        return itemService.createItem(itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") int itemId) {
        itemService.deleteItem(itemId);
    }

    @PatchMapping("/{id}")
    public ItemDto patchItem(
            @PathVariable("id") int id,
            @RequestBody ItemDto itemDto,
            @RequestHeader(value = ShareItServer.X_SHARER_USER_ID_HEADER_NAME) Optional<Integer> sharerUserId
    ) {
        itemDto.setId(id);
        itemDto.setOwnerId(sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new));
        return itemService.patchItem(itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(value = ShareItServer.X_SHARER_USER_ID_HEADER_NAME) Integer sharerUserId) {
        return itemService.getAllItems(sharerUserId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(
            @PathVariable("id") int itemId,
            @RequestHeader(value = ShareItServer.X_SHARER_USER_ID_HEADER_NAME) Integer sharerUserId
    ) {
        return itemService.getItem(itemId, sharerUserId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(
            @RequestParam("text") String searchText
    ) {
        return itemService.searchItem(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addItemComment(
            @PathVariable Integer itemId,
            @RequestBody CommentDto commentDto,
            @RequestHeader(value = ShareItServer.X_SHARER_USER_ID_HEADER_NAME) Integer sharerUserId
    ) {
        commentDto.setItemId(itemId);
        commentDto.setCreated(LocalDateTime.now());
        return itemService.addItemComment(commentDto, sharerUserId);
    }
}
