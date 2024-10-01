package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;
    private final ItemRequestServiceImpl itemRequestService;
    @Mock
    private final BookingServiceImpl bookingService;

    @Test
    void shouldBeAddedItemTest() {
        UserDto owner = userService.addUser(new UserDto(1L, "Test Owner", "testuser@yandex.ru"));
        ItemDto item = new ItemDto(
                2L,
                "name",
                "description",
                true,
                null);
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
    void shouldGetItemById() {
        UserDto owner = userService.addUser(new UserDto(1L, "Test Owner", "testuser@yandex.ru"));
        ItemDto item = new ItemDto(
                2L,
                "name",
                "description",
                true,
                null);
        ItemDto savedItem = itemService.addItem(item, owner.getId());
        ItemDatesCommentsDto getItem = itemService.getItemById(savedItem.getId(), owner.getId());
        assertThat(getItem.getId(), notNullValue());
        assertThat(getItem.getName(), equalTo(item.getName()));
    }

    @Test
    void shouldUpdateItem() {
        UserDto owner = userService.addUser(new UserDto(1L, "Test Owner", "testuser@yandex.ru"));
        ItemDto item = new ItemDto(
                2L,
                "name",
                "description",
                true,
                null);
        itemService.addItem(item, owner.getId());
        item.setName("new name");
        itemService.updateItem(item, owner.getId());
        ItemDatesCommentsDto getItem = itemService.getItemById(item.getId(), owner.getId());
        assertThat(getItem.getId(), notNullValue());
        assertThat(getItem.getName(), equalTo("new name"));
    }

    @Test
    void shouldGetItemsByOwnerId() {
        UserDto owner = userService.addUser(new UserDto(1L, "Test Owner", "testuser@yandex.ru"));
        ItemDto item = new ItemDto(
                2L,
                "name",
                "description",
                true,
                null);
        ItemDto savedItem = itemService.addItem(item, owner.getId());
        TypedQuery<Item> queryItem = em.createQuery("Select u from Item u where u.id = :id", Item.class);
        Item result = queryItem.setParameter("id", savedItem.getId())
                .getSingleResult();

        assertThat(result.getOwner().getId(), equalTo(owner.getId()));
    }

    @Test
    void shouldfindItemByText() {
        UserDto owner = userService.addUser(new UserDto(1L, "Test Owner", "testuser@yandex.ru"));
        ItemDto item = new ItemDto(
                2L,
                "name",
                "description",
                true,
                null);
        itemService.addItem(item, owner.getId());
        List<ItemDto> items = itemService.findItemByText(item.getDescription());
        assertThat(items.get(0).getName(), equalTo(item.getName()));
    }

/*
    @Test
    void shouldAddComment() {
        CommentRequestDto requestDto = new CommentRequestDto(
                "test comment"
        );
        UserDto owner = userService.addUser(new UserDto(0L,
                "Test Owner",
                "testuser@yandex.ru")
        );
        UserDto booker = userService.addUser(new UserDto(0L,
                "Test Booker",
                "testuser123@yandex.ru")
        );
        ItemDto item = new ItemDto(
                0L,
                "name",
                "description",
                true,
                null);

        ItemDto addedItem = itemService.addItem(item, owner.getId());

        BookingDto book = new BookingDto(addedItem.getId(),
                LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());

        when(bookingService.newBooking(book, booker.getId()))
                .thenReturn(new Booking(
                        1L,
                        BookingStatus.WAITING,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        em.find(Item.class, addedItem.getId()),
                        em.find(User.class, booker.getId()))
                );

        Booking newBooking = bookingService.newBooking(book, booker.getId());

        User userOwner = em.find(User.class, owner.getId());

        when(bookingService.updateBooking(newBooking.getId(), owner.getId(), true))
                .thenReturn(new Booking(
                        1L,
                        BookingStatus.WAITING,
                        LocalDateTime.now().minusDays(2),
                        LocalDateTime.now().minusDays(1),
                        new Item(addedItem.getId(),
                                "name",
                                "description",
                                userOwner,
                                true,
                                null),
                        em.find(User.class, booker.getId()))
                );

        Booking bookingUpdated = bookingService.updateBooking(newBooking.getId(), owner.getId(), true);

        CommentResponseDto commentResponseDto =
                itemService.addComment(requestDto, addedItem.getId(), booker.getId());
        assertThat(commentResponseDto.getText(), equalTo(requestDto.getText()));
    }
*/

}