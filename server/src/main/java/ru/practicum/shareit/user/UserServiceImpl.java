package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        return toUserDto(userRepository.create(toUser(userDto)));
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userRepository.delete(userId);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {
        return toUserDto(userRepository.update(toUser(user)));
    }

    @Override
    @Transactional
    public UserDto patchUser(UserDto user) {
        return toUserDto(userRepository.patch(toUser(user)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(int userId) {
        return toUserDto(userRepository.getUser(userId));
    }
}
