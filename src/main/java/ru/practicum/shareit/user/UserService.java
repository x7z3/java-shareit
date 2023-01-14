package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto user);

    void deleteUser(int userId);

    UserDto updateUser(UserDto user);

    UserDto patchUser(UserDto user);

    List<UserDto> getAllUsers();

    UserDto getUser(int userId);
}
