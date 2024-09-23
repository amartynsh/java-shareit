package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Обращение на POST /items со значением itemDto {} и X-Sharer-User-Id: {} ", itemDto, userId);
        return itemService.addItem(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDatesCommentsDto getItem(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDatesCommentsDto> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Обращение на GET /items со значением X-Sharer-User-Id: {} ", userId);
        return itemService.getItemsByOwnerId(userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId,
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

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestBody CommentRequestDto commentRequestDto, @PathVariable long itemId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Обращение на POST /items/{itemId}/comment со значением commentRequestDto {} и X-Sharer-User-Id: {} ",
                commentRequestDto, userId);
        return itemService.addComment(commentRequestDto, itemId, userId);
    }
}