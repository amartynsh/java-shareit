package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public Booking newBooking(BookingDto bookingDto, long userId) {
        log.info("Начало бронирования {} пользователя {}", bookingDto, userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("Пользователь не найден");
        }
        Optional<Item> itemOptional = itemRepository.findById(bookingDto.getItemId());
        if (!itemOptional.isPresent()) {
            throw new NotFoundException("Товар для бронирования не найден");
        }
        Item item = itemOptional.get();

        if (!item.getAvailable()) {
            throw new ValidationException("Этот товар недоступен для бронирования");
        }
        User user = userOptional.get();
        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Дата начала бронирования не может быть равна дате окончания");
        }

        Booking booking = BookingMapper.toBooking(bookingDto, user, item);
        bookingRepository.save(booking);
        return bookingRepository.getReferenceById(booking.getId());
    }

    public Booking updateBooking(Long bookingId, long userId, Boolean approved) {
        log.info("Обновление бронирования {}", bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException("Нельзя изменить чужое бронирование");
        }
        if (approved) {
            booking.setStatus(APPROVED);
            log.info("Бронирование принято");
        } else {
            booking.setStatus(REJECTED);
            log.info("Бронирование отклонено");
        }
        log.info("Бронирование обновлено");
        bookingRepository.save(booking);
        return booking;
    }

    public Booking getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new ValidationException("Нельзя получить чужое бронирование");
        }
        return booking;
    }

    public List<Booking> getBookingsByStatus(long userId, BookingRequestState state) {
        log.info("Получение бронирований по статусу {}", state);


        return switch (state) {
            case ALL -> bookingRepository.findAllByBookerId(userId);
            case WAITING -> bookingRepository.findAllByBookerIdIsAndStatusIsOrderByStartDesc(userId, WAITING);
            case REJECTED -> bookingRepository.findAllByBookerIdIsAndStatusIsOrderByStartDesc(userId, REJECTED);
            case CURRENT -> bookingRepository.findAllByBookerIdIsAndStatusIsOrderByStartDesc(userId, APPROVED);
            case FUTURE ->
                    bookingRepository.findAllByBookerIdIsAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case PAST -> bookingRepository.findAllByBookerIdIsAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
        };
    }

    public List<Booking> getBookingsByOwner(BookingRequestState state, long userId) {
        Set<Long> itemIds = Set.copyOf(itemRepository.findItemsByOwnerId(userId))
                .stream()
                .map(Item::getId)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        if (itemIds.isEmpty()) {
            throw new NotFoundException("Бронирование не найдено");
        }

        return switch (state) {
            case ALL -> bookingRepository.findAllByBookerIdIsAndItemIdInOrderByStartDesc(
                    userId, itemIds);
            case WAITING -> bookingRepository.findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(
                    userId, itemIds, WAITING);
            case REJECTED -> bookingRepository.findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(
                    userId, itemIds, REJECTED);
            case CURRENT -> bookingRepository.findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(
                    userId, itemIds, APPROVED);
            case FUTURE -> bookingRepository.findAllByBookerIdIsAndItemIdInAndStartAfterOrderByStartDesc(
                    userId, itemIds, LocalDateTime.now());
            case PAST -> bookingRepository.findAllByBookerIdIsAndItemIdInAndEndBeforeOrderByStartDesc(
                    userId, itemIds, LocalDateTime.now());
        };
    }
}