package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, long userId);

    ItemDto getItemById(long id);

    ItemDto updateItem(ItemDto itemDto, long userId);

    List<ItemDto> getItemsByOwnerId(long id);

    List<ItemDto> findItemByText(String text);
}