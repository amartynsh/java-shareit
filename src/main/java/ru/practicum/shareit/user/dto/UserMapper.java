package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());

    }

    public static User toUser(UserDto userDto) {
        if (userDto.getId() == null) {
            userDto.setId(0);
        }
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
}
