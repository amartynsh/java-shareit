package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseOwnerDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto createRequest(ItemRequestDto itemRequestDto, long authorId);

    List<ItemRequestResponseOwnerDto> getRequestByAuthorId(long authorId);

    ItemRequestResponseOwnerDto getRequestById(long rquestId);
}
