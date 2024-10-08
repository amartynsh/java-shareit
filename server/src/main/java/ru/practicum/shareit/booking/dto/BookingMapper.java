package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class BookingMapper {


    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setStart((LocalDateTime.parse(bookingDto.getStart())));
        booking.setEnd(LocalDateTime.parse(bookingDto.getEnd()));
        booking.setItem(item);
        return booking;
    }
}
