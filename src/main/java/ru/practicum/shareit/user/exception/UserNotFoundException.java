package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.user.User;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends ShareItException {
    public UserNotFoundException() {
        super("Queried user is not found");
    }

    public UserNotFoundException(Integer userId) {
        super(String.format("Queried user with id=%s is not found", userId));
    }

    public UserNotFoundException(User user) {
        super(String.format("User [%s] is not found", user));
    }
}
