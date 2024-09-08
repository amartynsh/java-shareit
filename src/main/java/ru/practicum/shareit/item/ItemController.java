package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final UserService userService;
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Обращение на POST /items со значением itemDto {} и X-Sharer-User-Id: {} ", itemDto, userId);
        Item item = ItemMapper.toItem(itemDto, userService.getUser(userId));
        return ItemMapper.toDto(itemService.addItem(item));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        return ItemMapper.toDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Обращение на GET /items со значением X-Sharer-User-Id: {} ", userId);

        List<ItemDto> items = itemService.getItemsByOwnerId(userId).stream().map(ItemMapper::toDto).toList();
        return items;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId) {
        log.info("Обращение на PATCH /items со значением itemDto {} и X-Sharer-User-Id: {} ", itemDto, userId);
        itemDto.setId(itemId);
        Item item = ItemMapper.toItem(itemDto, userService.getUser(userId));
        return ItemMapper.toDto(itemService.updateItem(item));
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByText(@PathParam("text") String text) {
        if (text.isBlank()) {
            return List.of();
        }
        if (text.isEmpty()) {
            return List.of();
        }
        log.info("Обращение на GET /items/search со значением text: {} ", text);
        return itemService.findItemByText(text).stream().map(ItemMapper::toDto).toList();
    }
}