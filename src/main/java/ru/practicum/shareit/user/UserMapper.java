package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
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
