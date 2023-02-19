package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.XSharerUserIdHeaderNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
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
            @RequestHeader(value = "X-Sharer-User-Id") Optional<Integer> sharerUserId
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
            @RequestBody @Valid ItemDto itemDto,
            @RequestHeader(value = "X-Sharer-User-Id") Optional<Integer> sharerUserId
    ) {
        itemDto.setId(id);
        itemDto.setOwnerId(sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new));
        return itemService.patchItem(itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") Integer sharerUserId) {
        return itemService.getAllItems(sharerUserId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(
            @PathVariable("id") int itemId,
            @RequestHeader(value = "X-Sharer-User-Id") Integer sharerUserId
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
            @RequestHeader(value = "X-Sharer-User-Id") Optional<Integer> sharerUserId
    ) {
        commentDto.setItemId(itemId);
        commentDto.setCreated(LocalDateTime.now());
        Integer userId = sharerUserId.orElseThrow(XSharerUserIdHeaderNotFoundException::new);
        return itemService.addItemComment(commentDto, userId);
    }
}
