package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {


    UserRepository repository;

    public User addUser(User user) {
        repository.emailIsUsed(user);
        return repository.add(user);
    }

    public User getUser(int id) {
        User user = repository.getUserById(id)
                .orElseThrow(() -> new ValidationException("User with this id not found"));
        log.info("Получили из репозитория пользователя = {} ", user);
        return user;
    }

    public UserDto updateUser(UserDto userDto) {
        User user = repository.getUserById(userDto.getId()).get();
        if (userDto.getEmail() == null) {
            log.info("дозаполнили email");
            userDto.setEmail(user.getEmail());
        }
        if (userDto.getName() == null) {
            log.info("дозаполнили имя");
            userDto.setName(user.getName());
        }
        User userToUpdate = UserMapper.toUser(userDto);
        repository.emailIsUsed(userToUpdate);
        log.info("Обновление пользователя = {} ", userToUpdate);
        return UserMapper.toUserDto(repository.update(userToUpdate));
    }

    public void deleteUser(int id) {
        repository.delete(id);
    }

}
