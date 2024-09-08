package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DublicateException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserRepositoryInmemoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private int count = 1;

    @Override
    public User add(User user) {
        user.setId(nextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен {}", user);
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void emailIsUsed(User user) {
        log.info("Проверка почты началась");
        for (User user1 : users.values()) {
            if (user.getEmail().equals(user1.getEmail()) && user1.getId() != user.getId()) {
                throw new DublicateException("Email is used");
            }
        }
        log.info("Проверка закончилась");
    }

    public void isUserExists(int id) {
        users.keySet().stream().filter(id1 -> id == id1)
                .findAny().orElseThrow(() -> new NotFoundException("User not found"));
    }

    private int nextId() {
        return count++;
    }
}