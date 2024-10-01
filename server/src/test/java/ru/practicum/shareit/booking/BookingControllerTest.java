package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    private final BookingDto bookingDto = new BookingDto(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1)
    );
    private final Booking booking = new Booking(
            1L,
            BookingStatus.WAITING,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(1),
            null,
            null);
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingServiceImpl bookingService;
    @Autowired
    private MockMvc mvc;

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
        when(bookingService.updateBooking(anyLong(), anyInt(), anyBoolean()))
                .thenReturn(booking);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())));
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
        when(bookingService.getBookingsByStatus(1L, BookingRequestState.valueOf(BookingRequestState.ALL.name())))
                .thenReturn(List.of(booking));

        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", String.valueOf(BookingRequestState.ALL)))
                .andExpect(status().isOk());
    }
}