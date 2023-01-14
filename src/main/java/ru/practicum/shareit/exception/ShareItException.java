package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShareItException extends RuntimeException {
    public ShareItException() {
        super();
    }

    public ShareItException(String message) {
        super(message);
    }

    public ShareItException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShareItException(Throwable cause) {
        super(cause);
    }

    protected ShareItException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
