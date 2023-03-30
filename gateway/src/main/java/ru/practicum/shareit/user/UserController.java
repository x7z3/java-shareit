package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        return userClient.createUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@Positive @PathVariable("id") int userId) {
        userClient.deleteUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@Positive @PathVariable("id") int userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> pathUser(
            @Positive @PathVariable("id") int id,
            @RequestBody UserDto userDto
    ) {
        return userClient.patchUser(id, userDto);
    }
}
