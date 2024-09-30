package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;


public class ItemMapper {

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getRequestId());
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), owner, itemDto.getAvailable(),
                itemDto.getRequestId());
    }

    public static ItemDatesCommentsDto toItemDatesCommentsDto(Item item, DatesDto lastBooking,
                                                              DatesDto nextBooking, List<CommentResponseDto> comments) {
        return new ItemDatesCommentsDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                comments, lastBooking, nextBooking);
    }

    public static ItemOwnerDto toItemOwnerDto(Item item) {
        return new ItemOwnerDto(item.getId(), item.getName(), item.getRequestId());
    }
}