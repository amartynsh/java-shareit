package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto getItemById(int id);

    ItemDto updateItem(ItemDto itemDto, int userId);

    List<ItemDto> getItemsByOwnerId(int id);

    List<ItemDto> findItemByText(String text);
}