package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
    User add(User user);

    Optional<User> getUserById(int id);

    void delete(int id);

    User update(User user);

    void emailIsUsed(User user);
}
