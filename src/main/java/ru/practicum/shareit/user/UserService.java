package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto getUser(int id);

    UserDto updateUser(UserDto userDto);

    void deleteUser(int id);
}