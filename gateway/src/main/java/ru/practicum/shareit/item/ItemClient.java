package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
        super(serverUrl, API_PREFIX);
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public void deleteItem(int itemId) {
        delete("/{itemId}", null, Map.of("itemId", itemId));
    }

    public ResponseEntity<Object> patchItem(Integer id, ItemDto itemDto, Long userId) {
        return patch("/{itemId}", userId, Map.of("itemId", id), itemDto);
    }

    public ResponseEntity<Object> getAllItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItem(int itemId, Long userId) {
        return get("/{itemId}", userId, Map.of("itemId", itemId));
    }

    public ResponseEntity<Object> searchItem(String searchText) {
        return get("/search?text={searchText}", null, Map.of("searchText", searchText));
    }

    public ResponseEntity<Object> addItemComment(Integer itemId, CommentDto commentDto, Long userId) {
        return post("/{itemId}/comment", userId, Map.of("itemId", itemId), commentDto);
    }
}
