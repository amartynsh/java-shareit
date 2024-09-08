package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {

    UserService userService;


    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Обращение на эндпоинт POST /users, пользователь: {}", userDto);
        User user = UserMapper.toUser(userDto);
        userService.addUser(user);
        return UserMapper.toUserDto(user);
    }

    //Тут наверное надо получать не Dto а просто User ?
    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        log.info("Обращение на эндпоинт PATCH /users/{}", id);
        userDto.setId(id);
        return userService.updateUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable int id) {
        log.info("Обращение на эндпоинт GET /users/{id}");
        return UserMapper.toUserDto(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

}
