package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.ShareItException;
import ru.practicum.shareit.user.User;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends ShareItException {
    public UserAlreadyExistsException(User user) {
        super(String.format("User [%s] already exists in the DB", user));
    }
}

