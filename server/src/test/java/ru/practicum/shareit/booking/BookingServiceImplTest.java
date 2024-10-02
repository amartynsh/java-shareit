package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private ItemDto item;
    private UserDto owner;
    private ItemDto addedItem;
    private BookingDto book;
    private UserDto booker;

    @BeforeEach
    void setUp() {
        item = new ItemDto(
                0L,
                "name",
                "description",
                true,
                null);
        owner = userService.addUser(new UserDto(0L,
                "Test Owner",
                "testuser@yandex.ru"));
        addedItem = itemService.addItem(item, owner.getId());
        book = new BookingDto(addedItem.getId(),
                LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        booker = userService.addUser(new UserDto(0L,
                "Test Booker",
                "testuser123@yandex.ru")
        );
    }


    @Test
    void shouldBeAddedBookingTest() {
        Booking booking = bookingService.newBooking(book, booker.getId());
        assertThat(booking.getBooker().getId(), equalTo(booker.getId()));
    }

    @Test
    void shouldNotBeAddedBookingTest() {
        BookingDto book = new BookingDto(item.getId(),
                LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        assertThrows(NotFoundException.class, () -> bookingService.newBooking(book, 22));
    }

    @Test
    void shoudGetBooking() {

        bookingService.newBooking(book, booker.getId());
        TypedQuery<Booking> queryBooking = em.createQuery("Select u from Booking u", Booking.class);
        Booking result = queryBooking
                .getSingleResult();

        bookingService.getBooking(result.getId(), booker.getId());
        assertThat(result.getBooker().getId(), equalTo(booker.getId()));
    }

    @Test
    void getBookingsByNonExistUserAndStateTest() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsByOwner(BookingRequestState.ALL, 11L));
    }

    @Test
    void getBookingsByUserAndCurrentStateTest() {
        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        List<Booking> result = bookingService.getBookingsByStatus(booker.getId(), BookingRequestState.CURRENT).stream().toList();
        assertThat(result, hasSize(0));
    }

    @Test
    void getBookingsByUserAndAllStateTest() {

        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        List<Booking> result = bookingService.getBookingsByStatus(booker.getId(), BookingRequestState.ALL).stream().toList();
        assertThat(result, hasSize(2));
    }

    @Test
    void getBookingsByUserAndWAITINGStateTest() {

        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        List<Booking> result = bookingService.getBookingsByStatus(booker.getId(), BookingRequestState.WAITING).stream().toList();
        assertThat(result, hasSize(2));
    }

    @Test
    void getBookingsByUserAndREJECTEDStateTest() {

        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        List<Booking> result = bookingService.getBookingsByStatus(booker.getId(), BookingRequestState.REJECTED).stream().toList();
        assertThat(result, hasSize(0));
    }

    @Test
    void getBookingsByUserAndFutureStateTest() {

        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        List<Booking> result = bookingService.getBookingsByStatus(booker.getId(), BookingRequestState.FUTURE).stream().toList();
        assertThat(result, hasSize(1));
    }

    @Test
    void getBookingsByUserAndPastStateTest() {

        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        List<Booking> result = bookingService.getBookingsByStatus(booker.getId(), BookingRequestState.PAST).stream().toList();
        assertThat(result, hasSize(1));
    }

    @Test
    void getBookingsByUserTest() {

        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsByOwner(BookingRequestState.CURRENT,
                booker.getId()));
    }

    @Test
    void getBookingsByUserAllStateTest() {
        ItemDto addedItem = itemService.addItem(item, owner.getId());
        BookingDto bookingRequestDto = new BookingDto(addedItem.getId(), LocalDateTime.now().plusHours(1).toString(),
                LocalDateTime.now().plusHours(10).toString());
        BookingDto bookingRequestDto2 = new BookingDto(addedItem.getId(), LocalDateTime.now().minusDays(2).toString(),
                LocalDateTime.now().minusDays(1).toString());
        bookingService.newBooking(bookingRequestDto, booker.getId());
        bookingService.newBooking(bookingRequestDto2, booker.getId());
        assertThrows(NotFoundException.class, () -> bookingService.getBookingsByOwner(BookingRequestState.ALL,
                booker.getId()));
    }

    @Test
    void shouldNotGetBookings() {
        ItemDto falseItem = new ItemDto(
                0L,
                "name",
                "description",
                false,
                null);
        ItemDto newItem = itemService.addItem(falseItem, owner.getId());
        book.setItemId(falseItem.getId());
        assertThrows(NotFoundException.class, () -> bookingService.newBooking(book, booker.getId()));

        book.setItemId(newItem.getId());
        assertThrows(ValidationException.class, () -> bookingService.newBooking(book, booker.getId()));

        book.setItemId(addedItem.getId());
        book.setEnd(book.getStart());
        assertThrows(ValidationException.class, () -> bookingService.newBooking(book, booker.getId()));
    }

    @Test
    void shouldUpdateBooking() {
        Booking booking = bookingService.newBooking(book, booker.getId());
        Booking bookUpdated = bookingService.updateBooking(booking.getId(), owner.getId(), true);
        assertThat(bookUpdated.getId(), equalTo(booking.getId()));
    }

    @Test
    void shouldNotUpdateBooking() {
        Booking booking = bookingService.newBooking(book, booker.getId());
        assertThrows(ValidationException.class, () ->
                bookingService.updateBooking(booking.getId(), 12L, true));
    }
}
