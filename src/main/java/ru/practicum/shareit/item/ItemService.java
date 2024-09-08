package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Item item);

    Item getItemById(int id);

    Item updateItem(Item item);

    List<Item> getItemsByOwnerId(int id);

    List<Item> findItemByText(String text);
}
