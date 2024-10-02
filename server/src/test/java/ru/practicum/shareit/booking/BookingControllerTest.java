package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {

    @Mock
    BookingServiceImpl bookingService;
    private BookingDto bookingDto = new BookingDto(
            1L,
            LocalDateTime.now().toString(),
            LocalDateTime.now().plusHours(1).toString()
    );
    private User user = new User(
            1L,
            "name",
            "ya.ya.ru"
    );
    private Item item = new Item(
            1L,
            "name",
            "description",
            user,
            true,
            null);
    private Booking booking = new Booking(
            1L,
            BookingStatus.WAITING,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            item,
            null);
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    @InjectMocks
    private BookingController controller;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void createBooking() throws Exception {
        when(bookingService.newBooking(any(), anyLong()))
                .thenReturn(booking);

        mvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
    }

    @Test
    public void changeBookingStatus() throws Exception {
        when(bookingService.updateBooking(1L, 1L, true))
                .thenReturn(booking);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void getBooking() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(booking);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
    }


    @Test
    public void getAllBookingsMadeByUser() throws Exception {
        when(bookingService.getBookingsByOwner(BookingRequestState.ALL, 1L))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(booking.getStatus().name())));
    }

    @Test
    public void getAllBookingsForByStatus() throws Exception {
        BookingRequestState state = BookingRequestState.ALL;
        when(bookingService.getBookingsByOwner(state, 1L))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", String.valueOf(BookingRequestState.ALL)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBookingsForByUserAndStatus() throws Exception {
        when(bookingService.getBookingsByOwner(BookingRequestState.valueOf(BookingRequestState.ALL.name()), 1L))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", String.valueOf(BookingRequestState.ALL)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBookingsForByStatusReject() throws Exception {
        when(bookingService.getBookingsByStatus(1L, BookingRequestState.valueOf(BookingRequestState.ALL.name())))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", String.valueOf(BookingRequestState.ALL)))
                .andExpect(status().isOk());
    }


    @Test
    public void getAllBookingsForByOwnerAndState() throws Exception {
        when(bookingService.getBookingsByOwner(BookingRequestState.CURRENT, 1L))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner/?state={state}", "CURRENT")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", String.valueOf(BookingRequestState.CURRENT)))
                .andExpect(status().isOk());
    }
}
