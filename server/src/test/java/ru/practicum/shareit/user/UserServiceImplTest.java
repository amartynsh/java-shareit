package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final EntityManager em;
    private final UserService userService;


    @Test
    void userShouldBeAdded() {
        UserDto userDto = new UserDto(null, "Test User", "test@yandex.ru");
        userService.addUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void userShouldBeUpdated() {
        UserDto userDto = new UserDto(null, "Test User", "test@yandex.ru");
        UserDto newUserSaved = userService.addUser(userDto);

        UserDto userToUpdateDto = new UserDto(newUserSaved.getId(), "Test User", "newemail@yandex.ru");
        UserDto user13 = userService.updateUser(userToUpdateDto);

        assertThat(user13.getEmail(), equalTo(userToUpdateDto.getEmail()));
    }

    @Test
    void userShouldBeDeleted() {
        UserDto userDto = new UserDto(null, "Test User", "test@yandex.ru");
        UserDto savedUserDto = userService.addUser(userDto);
        userService.deleteUser(savedUserDto.getId());
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        Assertions.assertThrows(NoResultException.class, () -> query.setParameter("email", userDto.getEmail())
                .getSingleResult());
    }
}
