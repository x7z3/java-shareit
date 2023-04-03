package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

@Service
public class UserClient extends BaseClient {
    public static final String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String basePath) {
        super(basePath, API_PREFIX);
    }

    public ResponseEntity<Object> createUser(UserDto userDto) {
        return post("", userDto);
    }

    public void deleteUser(int userId) {
        delete("/{id}", null, Map.of("id", userId));
    }

    public ResponseEntity<Object> getUser(int userId) {
        return get("/{id}", null, Map.of("id", userId));
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> patchUser(int id, UserDto userDto) {
        return patch("/{id}", null, Map.of("id", id), userDto);
    }
}
