package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, long userId) {
        if (itemDto.getName() == null) {
            throw new ValidationException("Name is required");
        }
        if (itemDto.getDescription() == null) {
            throw new ValidationException("Description is required");
        }
        Item item = ItemMapper.toItem(itemDto, UserMapper.toUser(userService.getById(userId)));
        checkCreatedItem(item);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDatesCommentsDto getItemById(long itemId, long userId) {
        Item item = itemRepository.findById(itemId).get();
        if (item.getOwner().getId() == userId) {
            return getItemByIdForOwner(item);
        }
        List<CommentResponseDto> commentsDto = commentRepository.findAllByItemId(itemId).stream().map(CommentMapper::toCommentResponseDto).toList();
        return ItemMapper.toItemDatesCommentsDto(item, null, null, commentsDto);
    }

    private ItemDatesCommentsDto getItemByIdForOwner(Item item) {
        DatesDto lastBookDate = bookingRepository.lastBookings(LocalDateTime.now(), item.getOwner().getId(), item.getId());
        DatesDto nextBookDate = bookingRepository.nextBookings(LocalDateTime.now(), item.getOwner().getId(), item.getId());
        List<CommentResponseDto> commentsDto = commentRepository.findAllByItemId(item.getId()).stream().map(CommentMapper::toCommentResponseDto).toList();
        return ItemMapper.toItemDatesCommentsDto(item, lastBookDate, nextBookDate, commentsDto);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        Item item = ItemMapper.toItem(itemDto, UserMapper.toUser(userService.getById(userId)));
        checkOwnerForUpdatedItem(item);
        fillLostValues(item);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public List<ItemDatesCommentsDto> getItemsByOwnerId(long id) {

        return itemRepository.getItemsByOwnerId(id).stream()
                .map(this::getItemByIdForOwner)
                .toList();
    }

    @Override
    public List<ItemDto> findItemByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        String lowerCaseText = text.toLowerCase();
        return itemRepository.findByNameOrDescriptionAllIgnoreCaseContaining(lowerCaseText, lowerCaseText).stream()
                .filter(item -> item.getAvailable())
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public CommentResponseDto addComment(CommentRequestDto commentRequestDto, long itemId, long userId) {
        Item item = itemRepository.findById(itemId).get();

        List<Booking> bookingsApproved = bookingRepository.findByItemIdAndBookerIdAndEndBeforeAndStatus(itemId, userId, LocalDateTime.now(),
                BookingStatus.APPROVED);


        if (bookingsApproved.isEmpty()) {
            throw new ValidationException("Пользователь не бронировал эту вещь");
        }

        Comment comment = CommentMapper.toComment(commentRequestDto, item,
                userRepository.findById(userId).get());

        commentRepository.save(comment);
        return CommentMapper.toCommentResponseDto(comment);
    }

    private void fillLostValues(Item item) {
        Item itemToUpdate = getItemByIdInternal(item.getId());
        if (item.getName() == null) {
            item.setName(itemToUpdate.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemToUpdate.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemToUpdate.getAvailable());
        }
        if (item.getOwner() == null) {
            item.setOwner(itemToUpdate.getOwner());
        }
    }

    public List<Comment> getCommentsByItemId(long itemId) {
        return commentRepository.findAllByItemId(itemId);
    }

    private void checkCreatedItem(Item item) {
        if (item.getAvailable() == null) {
            throw new ValidationException("Не указано поле Available");
        }
        if (item.getName().isBlank()) {
            throw new ValidationException("Не указано поле Name");
        }
        if (item.getDescription().isBlank()) {
            throw new ValidationException("Не указано поле Description");
        }
    }

    private void checkOwnerForUpdatedItem(Item item) {
        Item itemExisting = itemRepository.getReferenceById(item.getId());
        if (itemExisting.getOwner().getId() != item.getOwner().getId()) {
            throw new ValidationException("Владелец предмета" + itemExisting.getOwner() + " и пользователя" +
                    item.getOwner() + " не совпадают");
        }
    }

    private Item getItemByIdInternal(long id) {
        return itemRepository.getReferenceById(id);
    }
}