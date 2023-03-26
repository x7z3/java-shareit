package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User create(User user);

    void delete(int userId);

    User update(User user);

    List<User> getAllUsers();

    User getUser(int userId);

    User patch(User user);
}
