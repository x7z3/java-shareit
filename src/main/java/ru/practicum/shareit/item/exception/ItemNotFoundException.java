package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends ShareItException {
    public ItemNotFoundException() {
        super("Item is not found");
    }

    public ItemNotFoundException(Integer itemId) {
        super(String.format("Item with ID=%s is not found", itemId));
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
