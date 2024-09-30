package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription(itemRequest.getDescription());
        return itemRequestDto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User author) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setAuthor(author);
        return itemRequest;
    }

    public static ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setId(itemRequest.getId());
        itemRequestResponseDto.setDescription(itemRequest.getDescription());
        itemRequestResponseDto.setCreated(itemRequest.getCreated());
        return itemRequestResponseDto;
    }

    public static ItemRequestResponseOwnerDto toItemRequestResponseOwnerDto(ItemRequest itemRequest, List<ItemOwnerDto> itemOwnerDtos) {
        ItemRequestResponseOwnerDto itemRequestResponseOwnerDto = new ItemRequestResponseOwnerDto();
        itemRequestResponseOwnerDto.setId(itemRequest.getId());
        itemRequestResponseOwnerDto.setDescription(itemRequest.getDescription());
        itemRequestResponseOwnerDto.setCreated(itemRequest.getCreated());
        itemRequestResponseOwnerDto.setItems(itemOwnerDtos);
        return itemRequestResponseOwnerDto;
    }


}
