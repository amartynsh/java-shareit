package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    Item getItem(int id);

    void deleteItem(int id);

    Item updateItem(Item item);

    List<Item> getItemsByOwner(int ownerId);

    List<Item> findItemByText(String text);
}
