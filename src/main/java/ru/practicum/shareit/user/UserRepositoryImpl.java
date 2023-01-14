package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserRepositoryImpl extends SimpleJpaRepository<User, Integer> implements UserRepository {
    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public User create(User user) {
        emailShouldNotExist(user);
        return super.save(user);
    }

    @Override
    public void delete(int userId) {
        super.delete(findUserById(userId));
    }

    @Override
    public User update(User user) {
        User foundUser = findUserById(user.getId());
        foundUser.setName(user.getName());
        foundUser.setEmail(user.getEmail());
        return foundUser;
    }

    @Override
    public User patch(User user) {
        User foundUser = findUserById(user.getId());
        if (user.getEmail() != null) {
            emailShouldNotExist(user);
            foundUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            foundUser.setName(user.getName());
        }
        return foundUser;
    }

    @Override
    public List<User> getAllUsers() {
        return findAll();
    }

    @Override
    public User getUser(int userId) {
        return findUserById(userId);
    }

    private void emailShouldNotExist(User user) {
        findOne((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), user.getEmail()))
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException(user);
                });
    }

    private User findUserById(Integer userId) {
        return findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
