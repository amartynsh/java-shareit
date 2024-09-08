package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private int count = 1;

    @Override
    public Item createItem(Item item) {
        item.setId(nextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItem(int id) {
        return items.get(id);
    }

    @Override
    public void deleteItem(int id) {
        items.remove(id);
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getItemsByOwner(int ownerId) {
        return items.values().stream().filter(item -> item.getOwner().getId() == ownerId)
                .toList();
    }

    @Override
    public List<Item> findItemByText(String text) {
        return items.values().stream().filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    private int nextId() {
        return count++;
    }
}