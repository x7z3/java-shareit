package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan("ru.practicum.shareit")
class UserRepositoryTest {
    private UserRepository userRepository;
    private User user;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;

        user = userRepository.create(new User(1, "name", "name@mail.ru"));
    }

    @Test
    void create() {
        User resultUser = userRepository.create(user);
        assertNotNull(resultUser);
    }

    @Test
    void delete() {
        User createdUser = userRepository.create(user);
        assertDoesNotThrow(() -> userRepository.delete(createdUser.getId()));
    }

    @Test
    void update() {
        User update = userRepository.update(user);
        assertNotNull(update);
    }

    @Test
    void getAllUsers() {
        List<User> allUsers = userRepository.getAllUsers();
        assertNotNull(allUsers);
        assertFalse(allUsers.isEmpty());
    }

    @Test
    void getUser() {
        User resultUser = userRepository.getUser(user.getId());
        assertNotNull(resultUser);
    }

    @Test
    void patch() {
        User patch = userRepository.patch(user);
        assertNotNull(patch);
    }
}