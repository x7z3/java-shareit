package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WrongItemOwnerException extends ShareItException {
    public WrongItemOwnerException(@NotBlank Item item, Integer userId) {
        super(String.format("User ID=%s doesn't match requested Item's user ID=%s", userId, item.getOwner().getId()));
    }
}
