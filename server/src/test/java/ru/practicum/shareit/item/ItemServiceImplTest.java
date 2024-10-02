package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {

    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;
    private final ItemRequestServiceImpl itemRequestService;

    private final BookingServiceImpl bookingService;
    UserDto owner;
    ItemDto item;

    @BeforeEach
    void setUp() {
        item = new ItemDto(
                2L,
                "name",
                "description",
                true,
                null);
        owner = userService.addUser(new UserDto(1L, "Test Owner", "testuser@yandex.ru"));
    }

    @Test
    void shouldBeAddedItemTest() {
        item = itemService.addItem(item, owner.getId());
        TypedQuery<Item> queryItem = em.createQuery("Select u from Item u where u.id = :id", Item.class);
        Item result = queryItem.setParameter("id", item.getId())
                .getSingleResult();

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(item.getName()));
        assertThat(result.getDescription(), equalTo(item.getDescription()));
        assertThat(result.getOwner().getId(), equalTo(owner.getId()));
    }

    @Test
    void shouldNotBeAddedItemTest() {
        item.setAvailable(null);
        assertThrows(ValidationException.class, () -> itemService.addItem(item, owner.getId()));
    }

    @Test
    void shouldGetItemById() {
        ItemDto savedItem = itemService.addItem(item, owner.getId());
        ItemDatesCommentsDto getItem = itemService.getItemById(savedItem.getId(), owner.getId());
        assertThat(getItem.getId(), notNullValue());
        assertThat(getItem.getName(), equalTo(item.getName()));
    }

    @Test
    void shouldUpdateItem() {
        ItemDto newItem = itemService.addItem(item, owner.getId());
        newItem.setName("new name");
        ItemDto newItemUpdated = itemService.updateItem(newItem, owner.getId());
        assertThat(newItemUpdated.getId(), notNullValue());
        assertThat(newItemUpdated.getName(), equalTo("new name"));
    }

    @Test
    void shouldUpdateItemAndFillLostName() {
        ItemDto newItem = itemService.addItem(item, owner.getId());
        newItem.setName(null);
        newItem.setDescription(null);
        newItem.setAvailable(null);
        ItemDto newItemUpdated = itemService.updateItem(newItem, owner.getId());
        assertThat(newItemUpdated.getId(), notNullValue());
        assertThat(newItemUpdated.getName(), equalTo("name"));
        assertThat(newItemUpdated.getDescription(), equalTo(item.getDescription()));
        assertThat(newItemUpdated.getAvailable(), equalTo(item.getAvailable()));


    }

    @Test
    void shouldNotUpdateItem() {
        UserDto requestor = userService.addUser(new UserDto(0L,
                "Test Owner",
                "testrequester@yandex.ru"));
        ItemDto newItem = itemService.addItem(item, owner.getId());
        newItem.setName("new name");
        assertThrows(ValidationException.class, () -> itemService.updateItem(newItem, requestor.getId()));
    }

    @Test
    void shouldGetItemsByOwnerId() {
        ItemDto savedItem = itemService.addItem(item, owner.getId());
        TypedQuery<Item> queryItem = em.createQuery("Select u from Item u where u.id = :id", Item.class);
        Item result = queryItem.setParameter("id", savedItem.getId())
                .getSingleResult();

        assertThat(result.getOwner().getId(), equalTo(owner.getId()));
    }

    @Test
    void shouldfindItemByText() {
        itemService.addItem(item, owner.getId());
        List<ItemDto> items = itemService.findItemByText(item.getDescription());
        assertThat(items.get(0).getName(), equalTo(item.getName()));
    }

    @Test
    void shouldNotfindItemByText() {
        itemService.addItem(item, owner.getId());
        List<ItemDto> items = itemService.findItemByText("");
        assertThat(items.size(), equalTo(0));
    }

    @Test
    void shouldNotAddItemTest() {
        item.setAvailable(null);
        assertThrows(ValidationException.class, () -> itemService.addItem(item, owner.getId()));
        item.setAvailable(true);
        item.setName("");
        assertThrows(ValidationException.class, () -> itemService.addItem(item, owner.getId()));
        item.setName("Name");
        item.setDescription("");
        assertThrows(ValidationException.class, () -> itemService.addItem(item, owner.getId()));
    }

    @Test
    void shouldGetItemsByUserId() {
        UserDto requestor = userService.addUser(new UserDto(1L, "Test Owner", "testrequester@yandex.ru"));
        ItemDto savedItem = itemService.addItem(item, owner.getId());
        ItemDatesCommentsDto newItem = itemService.getItemById(savedItem.getId(), requestor.getId());
        assertThat(newItem.getId(), equalTo(savedItem.getId()));
        assertThat(newItem.getComments().size(), equalTo(0));
    }

    @Test
    void shouldMapCommentTest() {
        CommentRequestDto requestDto = new CommentRequestDto(
                "test comment"
        );

        User user = new User(
                1L,
                "test user",
                "test@yandex.ru"
        );
        Item newItem = new Item(
                2L,
                "name",
                "description",
                user,
                true,
                null
        );

        CommentMapper.toComment(requestDto, newItem, user);
        Comment comment = new Comment(
                1L,
                "text",
                user,
                newItem,
                LocalDateTime.now());
        CommentMapper.toCommentResponseDto(comment);
    }

    @Test
    void shouldGetItemsByOwner() {
        itemService.addItem(item, owner.getId());
        List<ItemDatesCommentsDto> items = itemService.getItemsByOwnerId(owner.getId());
        assertThat(items.size(), equalTo(1));
    }


}