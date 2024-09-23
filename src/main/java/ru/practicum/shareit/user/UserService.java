package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto getById(long id);

    UserDto updateUser(UserDto userDto);

    void deleteUser(long id);

}