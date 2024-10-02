package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody BookingDto bookingDto) {
        log.info("Обращение на POST /bookings {}", bookingDto);
        return bookingService.newBooking(bookingDto, userId);
    }

    @PatchMapping(path = "/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId,
                                 @RequestParam Boolean approved) {
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @GetMapping(path = "/{bookingId}")
    public Booking getBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        bookingService.getBooking(bookingId, userId);
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getBookingsByStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(defaultValue = "ALL") BookingRequestState state) {
        log.info("Обращение на GET /bookings?state={} ", state, userId);
        return bookingService.getBookingsByStatus(userId, state);
    }

    @GetMapping(path = "/owner")
    public List<Booking> getBookingsByOwnerState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") BookingRequestState state) {
        log.info("Обращение на GET /bookings/owner?state={} и  userId={}", state, userId);
        return bookingService.getBookingsByOwner(state, userId);
    }
}
