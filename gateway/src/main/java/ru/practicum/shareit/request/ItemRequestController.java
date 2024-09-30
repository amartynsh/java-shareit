package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ItemRequestDto request, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.createItemRequest(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getByUserId(userId);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<Object> getByRequestId(@PathVariable long requestId) {
        return itemRequestClient.getByRequestId(requestId);
    }

}
