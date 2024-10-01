package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;

    @Test
    public void findByIdTest() {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("name");
        userToCreate.setEmail("email");
        long userId = userService.addUser(userToCreate).getId();

        ItemDto itemToCreate = new ItemDto(
                1L,
                "name",
                "description",
                true,
                null
        );
        itemToCreate.setAvailable(true);
        itemToCreate.setDescription("description");
        itemToCreate.setName("name");
        ItemDto itemCreated= itemService.addItem(itemToCreate, userId);

        assertThat(itemCreated.getId(), notNullValue());
        assertThat(itemCreated.getName(), is(equalTo(itemToCreate.getName())));
        assertThat(itemCreated.getDescription(), is(equalTo(itemToCreate.getDescription())));
        assertThat(itemCreated.getAvailable(), is(equalTo(itemToCreate.getAvailable())));
    }
}