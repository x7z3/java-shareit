package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    private UserMapper() {
    }

    public static User toUser(UserDto userDto) {
        if (userDto == null) return new User();
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static UserDto toUserDto(User user) {
        if (user == null) return new UserDto();
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
