package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDatesCommentsDto getItemById(long id, long userId);

    ItemDto updateItem(ItemDto itemDto, long userId);

    List<ItemDatesCommentsDto> getItemsByOwnerId(long id);

    List<ItemDto> findItemByText(String text);

    CommentResponseDto addComment(CommentRequestDto commentRequestDto, long itemId, long userId);
}