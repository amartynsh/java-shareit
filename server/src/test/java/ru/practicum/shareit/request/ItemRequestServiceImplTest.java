package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseOwnerDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;
    private final ItemRequestServiceImpl itemRequestService;


    ItemRequestDto itemRequestDto = new ItemRequestDto("description");
    ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto(
            1L,
            "description",
            LocalDateTime.now());

    @Test
    void shouldCreateItemRequest() {
        UserDto userDto = new UserDto(1L, "Test User", "test@yandex.ru");
        UserDto savedUser = userService.addUser(userDto);
        ItemRequestResponseDto savedRequest = itemRequestService.createRequest(itemRequestDto, savedUser.getId());
        TypedQuery<ItemRequest> queryItem = em.createQuery("Select ir from ItemRequest ir where ir.id = :id", ItemRequest.class);
        ItemRequest result = queryItem.setParameter("id", savedRequest.getId())
                .getSingleResult();
        assertThat(result.getDescription(), equalTo(itemRequestDto.getDescription()));

    }

    @Test
    void shouldNotCreateItemRequest() {
        UserDto userDto = new UserDto(1L, "Test User", "test@yandex.ru");
        userService.addUser(userDto);
        assertThrows(NoSuchElementException.class, () -> itemRequestService.createRequest(itemRequestDto, 2L));
    }

    @Test
    void shouldGetRequestById() {
        UserDto userDto = new UserDto(2L, "Test User", "test1@yandex.ru");
        UserDto savedUser = userService.addUser(userDto);
        ItemRequestResponseDto savedRequest = itemRequestService.createRequest(itemRequestDto, savedUser.getId());
        ItemRequestResponseOwnerDto result = itemRequestService.getRequestById(savedRequest.getId());
        assertThat(result.getId(), equalTo(savedRequest.getId()));
    }

    @Test
    void shouldGetRequestByAuthorId() {
        UserDto userDto1 = new UserDto(2L, "Request Owner", "test@yandex.ru");

        UserDto savedUser1 = userService.addUser(userDto1);

        ItemRequestResponseDto savedRequest1 = itemRequestService.createRequest(itemRequestDto, savedUser1.getId());
        List<ItemRequestResponseOwnerDto> result1 = itemRequestService.getRequestByAuthorId(savedUser1.getId());
        assertThat(result1.get(0).getId(), equalTo(savedRequest1.getId()));
    }

    @Test
    void shouldGetByAuthorId() {
        UserDto userDto1 = new UserDto(0L, "Request Owner", "test23@yandex.ru");
        UserDto savedUser1 = userService.addUser(userDto1);

        ItemRequestResponseDto savedRequest1 = itemRequestService.createRequest(itemRequestDto, savedUser1.getId());
        List<ItemRequestResponseOwnerDto> result1 = itemRequestService.getRequestByAuthorId(savedUser1.getId());
        assertThat(result1.get(0).getId(), equalTo(savedRequest1.getId()));
    }


}