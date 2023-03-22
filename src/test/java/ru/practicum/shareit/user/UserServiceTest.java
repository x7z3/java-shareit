package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final UserDto userDto = new UserDto(1, "name", "name@mail.ru");
    private final User user = new User(1, "name", "name@mail.ru");

    @Test
    void createUser() {
        Mockito.when(userRepository.create(isA(User.class))).thenReturn(user);

        UserDto user = userService.createUser(userDto);
        assertNotNull(user);

        Mockito.verify(userRepository, times(1)).create(isA(User.class));
    }

    @Test
    void deleteUser() {
        Mockito.doAnswer((Answer<Void>) instance -> null).when(userRepository).delete(anyInt());

        assertDoesNotThrow(() -> userService.deleteUser(1));

        Mockito.verify(userRepository, times(1)).delete(anyInt());
    }

    @Test
    void updateUser() {
        Mockito.when(userRepository.update(isA(User.class))).thenReturn(user);

        UserDto resultUser = userService.updateUser(userDto);
        assertNotNull(resultUser);

        Mockito.verify(userRepository, times(1)).update(isA(User.class));
    }

    @Test
    void patchUser() {
        Mockito.when(userRepository.patch(isA(User.class))).thenReturn(user);

        UserDto resultUser = userService.patchUser(userDto);
        assertNotNull(resultUser);

        Mockito.verify(userRepository, times(1)).patch(isA(User.class));
    }

    @Test
    void getAllUsers() {
        Mockito.when(userRepository.getAllUsers()).thenReturn(List.of(user));

        List<UserDto> allUsers = userService.getAllUsers();
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());

        Mockito.verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    void getUser() {
        Mockito.when(userRepository.getUser(anyInt())).thenReturn(user);

        UserDto resultUser = userService.getUser(1);
        assertNotNull(resultUser);

        Mockito.verify(userRepository, times(1)).getUser(1);
    }
}