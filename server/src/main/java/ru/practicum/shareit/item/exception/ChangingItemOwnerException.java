package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.model.Item;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ChangingItemOwnerException extends ShareItException {

    public ChangingItemOwnerException() {
        super("Unauthorized attempt to change an Item owner");
    }

    public ChangingItemOwnerException(Item item) {
        super(String.format("Unauthorized attempt to change persistent Item [%s] owner", item));
    }
}
