package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.DatesDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    List<Booking> findAllByBookerId(Long id);

    List<Booking> findAllByBookerIdAndItemId(long userId, long itemId);

    List<Booking> findAllByBookerIdIsAndStatusIsOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findAllByBookerIdIsAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findAllByBookerIdIsAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findAllByBookerIdIsAndItemIdInOrderByStartDesc(long userId, Set<Long> itemIds);

    List<Booking> findAllByBookerIdIsAndItemIdInAndStatusIsOrderByStartDesc(long userId, Set<Long> itemIds,
                                                                            BookingStatus status);

    List<Booking> findAllByBookerIdIsAndItemIdInAndStartAfterOrderByStartDesc(long userId, Set<Long> itemIds,
                                                                              LocalDateTime start);

    List<Booking> findAllByBookerIdIsAndItemIdInAndEndBeforeOrderByStartDesc(long userId, Set<Long> itemIds,
                                                                             LocalDateTime end);

    List<Booking> findAllByBookerIdIsAndItemIdInAndStartAfterOrderByStartDesc(
            Long ownerId, Set<Long> itemIds, LocalDateTime now);

    List<Booking> findAllByBookerIdIsAndItemIdInAndEndBeforeOrderByStartDesc(
            Long ownerId, Set<Long> itemIds, LocalDateTime now);

    List<Booking> findByItemIdAndBookerIdAndEndBeforeAndStatus(long itemId, long bookerId,
                                                               LocalDateTime end, BookingStatus status);

    @Query(" select new ru.practicum.shareit.item.dto.DatesDto (MAX(b.start), MAX(b.end)) " +
            " from Booking b " +
            " join b.item i " +
            " join i.owner o " +
            "where b.start < ?1 " +
            "  and o.id = ?2 " +
            " and i.id =?3 ")
    DatesDto lastBookings(LocalDateTime date, long ownerId, long itemId);

    @Query(" select new ru.practicum.shareit.item.dto.DatesDto(MIN(b.start), MIN(b.end)) " +
            " from Booking b " +
            " join b.item i " +
            " join i.owner o " +
            "where b.start >= ?1 " +
            "  and o.id = ?2 " +
            " and i.id =?3 ")
    DatesDto nextBookings(LocalDateTime date, long ownerId, long itemId);

    List<Item> findAllByBookerIdAndItemIdAndStatus(long userId, long itemId, BookingStatus status);
}
