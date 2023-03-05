package ru.practicum.shareit.exception;

public class XSharerUserIdHeaderNotFoundException extends ShareItException {
    public XSharerUserIdHeaderNotFoundException() {
        super("X-Sharer-User-Id header is not found");
    }
}
