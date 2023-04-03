package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {
    public static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String basePath) {
        super(basePath, API_PREFIX);
    }

    public ResponseEntity<Object> createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public ResponseEntity<Object> getItemRequests(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemRequests(Integer from, Integer size, Long userId) {
        return get("/all?from={from}&size={size}", userId, Map.of("from", from, "size",size));
    }

    public ResponseEntity<Object> getItemRequest(Integer itemRequestId, Long userId) {
        return get("/{id}", userId, Map.of("id", itemRequestId));
    }
}
