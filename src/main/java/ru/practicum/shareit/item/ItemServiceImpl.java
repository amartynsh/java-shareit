package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        Item item = ItemMapper.toItem(itemDto, UserMapper.toUser(userService.getUser(userId)));
        checkCreatedItem(item);
        return ItemMapper.toDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto getItemById(int id) {
        return ItemMapper.toDto(itemRepository.getItem(id));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int userId) {
        Item item = ItemMapper.toItem(itemDto,UserMapper.toUser(userService.getUser(userId)));
        checkOwner(item);
        fillLostValues(item);
        return ItemMapper.toDto(itemRepository.updateItem(item));
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(int id) {
        return itemRepository.getItemsByOwner(id).stream().map(ItemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> findItemByText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.findItemByText(text).stream().map(ItemMapper::toDto).toList();
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
        if (itemExisting.getOwner().getId() != item.getOwner().getId()) {
            throw new ValidationException("Владелец предмета" + itemExisting.getOwner() + " и пользователя" +
                    item.getOwner() + " не совпадают");
        }
    }

    private Item getItemByIdInternal(int id) {
        return itemRepository.getItem(id);
    }
}