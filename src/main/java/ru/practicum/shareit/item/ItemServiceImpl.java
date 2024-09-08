package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;

    @Override
    public Item addItem(Item item) {
        checkCreatedItem(item);
        return itemRepository.createItem(item);
    }

    @Override
    public Item getItemById(int id) {
        return itemRepository.getItem(id);
    }

    @Override
    public Item updateItem(Item item) {
        checkOwner(item);
        fillLostValues(item);
        return itemRepository.updateItem(item);
    }

    @Override
    public List<Item> getItemsByOwnerId(int id) {
        return itemRepository.getItemsByOwner(id);
    }

    @Override
    public List<Item> findItemByText(String text) {
        return itemRepository.findItemByText(text);
    }

    private void fillLostValues(Item item) {
        Item itemToUpdate = getItemById(item.getId());
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

    private void checkOwner(Item item) {
        Item itemExisting = itemRepository.getItem(item.getId());
        if (itemExisting.getOwner() != item.getOwner()) {
            throw new ValidationException("Обновлять данные может только владелец");
        }
    }

}

