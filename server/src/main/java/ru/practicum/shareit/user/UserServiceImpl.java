package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DublicateException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        checkByEmail(user);
        User savedUser = repository.save(user);
        log.info("Сохранили пользователя = {} ", savedUser);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto getById(long id) {
        User user = repository.getReferenceById(id);
        log.info("Получили из репозитория пользователя = {} ", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("начали обновление пользователя = {} ", userDto);
        User user = repository.getReferenceById(userDto.getId());

        log.info("получили из репозиторияпользователя = {} ", user);
        if (userDto.getEmail() == null) {
            userDto.setEmail(user.getEmail());
        }

        if (userDto.getName() == null) {
            userDto.setName(user.getName());
        }
        User userToUpdate = UserMapper.toUser(userDto);
        checkByEmail(userToUpdate);
        return UserMapper.toUserDto(repository.save(userToUpdate));
    }

    @Override
    public void deleteUser(long id) {
        log.info("Удаление пользователя с id = {} ", id);
        repository.deleteById(id);
    }

    private void checkByEmail(User user) {

        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("User email is null");
        }
        var userByEmail = Optional.ofNullable(repository.findByEmailIgnoreCase(user.getEmail()));
        if (userByEmail.isPresent()) {
            if (userByEmail.get().getId() != user.getId()) {
                throw new DublicateException("User with this email already exists");
            }
        }
    }
}