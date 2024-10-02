package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;
    private final ItemRequestServiceImpl itemRequestService;

    private final BookingServiceImpl bookingService;
    @Mock
    private final BookingRepository bookingRepository;

    @Test
    void shouldBeAddedBookingTest() {
        ItemDto item = new ItemDto(
                0L,
                "name",
                "description",
                true,
                null);
        UserDto owner = userService.addUser(new UserDto(0L,
                "Test Owner",
                "testuser@yandex.ru")
        );

        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto book = new BookingDto(addedItem.getId(),
                LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        UserDto booker = userService.addUser(new UserDto(0L,
                "Test Booker",
                "testuser123@yandex.ru")
        );
        Booking booking = bookingService.newBooking(book, booker.getId());
        assertThat(booking.getBooker().getId(), equalTo(booker.getId()));
    }

    @Test
    void shoudGetBooking() {
        UserDto owner = userService.addUser(new UserDto(0L,
                "Test Owner",
                "testuser@yandex.ru")
        );
        ItemDto item = new ItemDto(
                0L,
                "name",
                "description",
                true,
                null);

        ItemDto addedItem = itemService.addItem(item, owner.getId());

        UserDto booker = userService.addUser(new UserDto(0L,
                "Test Booker",
                "testuser123@yandex.ru"));
        BookingDto book = new BookingDto(addedItem.getId(),
                LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(book, booker.getId());
        TypedQuery<Booking> queryBooking = em.createQuery("Select u from Booking u", Booking.class);
        Booking result = queryBooking
                .getSingleResult();

        bookingService.getBooking(result.getId(), booker.getId());
        assertThat(result.getBooker().getId(), equalTo(booker.getId()));
    }

/*    @Test
    void shouldUpdateBooking() {
        UserDto owner = userService.addUser(new UserDto(0L,
                "Test Owner",
                "testuser@yandex.ru")
        );
        ItemDto item = new ItemDto(
                0L,
                "name",
                "description",
                true,
                null);

        ItemDto addedItem = itemService.addItem(item, owner.getId());

        UserDto booker = userService.addUser(new UserDto(0L,
                "Test Booker",
                "testuser123@yandex.ru"));
        BookingDto book = new BookingDto(addedItem.getId(),
                LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        Booking booking = bookingService.newBooking(book, booker.getId());
        TypedQuery<Booking> queryBooking = em.createQuery("Select u from Booking u", Booking.class);
        Booking result = queryBooking
                .getSingleResult();


        bookingService.updateBooking(result.getId(), owner.getId(), true);

        Booking result2 = queryBooking
                .getSingleResult();
        assertThat(result.getStatus(), equalTo(true));

    }*/
}
