package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final UserService userService;
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Обращение на POST /items со значением itemDto {} и X-Sharer-User-Id: {} ", itemDto, userId);
        return itemService.addItem(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Обращение на GET /items со значением X-Sharer-User-Id: {} ", userId);
        return itemService.getItemsByOwnerId(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId) {
        log.info("Обращение на PATCH /items со значением itemDto {} и X-Sharer-User-Id: {} ", itemDto, userId);
        itemDto.setId(itemId);
        return itemService.updateItem(itemDto, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByText(@PathParam("text") String text) {
        log.info("Обращение на GET /items/search со значением text: {} ", text);
        return itemService.findItemByText(text);
    }
}