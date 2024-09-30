package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseOwnerDto;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestServiceImpl requestService;

    @PostMapping
    public ItemRequestResponseDto addRequest(@RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Обращение на POST /requests, ItemRequestDto: {}, userId: {}",  itemRequestDto.toString(), userId);
        return requestService.createRequest(itemRequestDto, userId);

    }

    @GetMapping
    public List<ItemRequestResponseOwnerDto> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestByAuthorId(userId);
    }

    @GetMapping(path = "/{itemId}")
    ItemRequestResponseOwnerDto getRequestById(@PathVariable("itemId") long itemId) {
        return requestService.getRequestById(itemId);
    }

}
