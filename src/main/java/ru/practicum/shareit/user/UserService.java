package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    User addUser(User user);

    User getUser(int id);

    UserDto updateUser(UserDto userDto);

    void deleteUser(int id);
}
