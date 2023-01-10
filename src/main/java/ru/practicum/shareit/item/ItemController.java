package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
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
        itemDto.setOwnerId(sharerUserId.orElseThrow(() -> new ShareItException("X-Sharer-User-Id header is not found")));
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
        itemDto.setOwnerId(sharerUserId.orElseThrow(() -> new ShareItException("X-Sharer-User-Id header is not found")));
        return itemService.patchItem(itemDto);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") Integer sharerUserId) {
        return itemService.getAllItems(sharerUserId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable("id") int itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(
            @RequestParam("text") String searchText
    ) {
        return itemService.searchItem(searchText);
    }
}
