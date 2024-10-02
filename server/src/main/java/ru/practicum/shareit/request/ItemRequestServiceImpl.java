package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseOwnerDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public ItemRequestResponseDto createRequest(ItemRequestDto itemRequestDto, long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("Пользователь ID = " + authorId + " не найден"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, author);
        ItemRequestResponseDto itemRequestResponseDto = ItemRequestMapper.toItemRequestResponseDto(itemRequestRepository.save(itemRequest));
        log.info("Сохранили itemRequestResponseDto = {}", itemRequestResponseDto);
        return itemRequestResponseDto;
    }

    @Override
    public List<ItemRequestResponseOwnerDto> getRequestByAuthorId(long authorId) {
        log.info("Начало обработки запроса getRequestByAuthorId = {}", authorId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByAuthorIdOrderByCreatedDesc(authorId);
        log.info("Получили список findByAuthorIdOrderByCreatedDesc = {}", itemRequests);

        return itemRequests.stream().map(this::getItemRequestsResponseWithAnswers).toList();
    }

    @Override
    public ItemRequestResponseOwnerDto getRequestById(long rquestId) {
        log.info("Начало обработки запроса getRequestById = {}", rquestId);
        ItemRequest itemRequests = itemRequestRepository.findById(rquestId).get();

        return getItemRequestsResponseWithAnswers(itemRequests);
    }

    private ItemRequestResponseOwnerDto getItemRequestsResponseWithAnswers(ItemRequest itemRequest) {

        List<ItemOwnerDto> itemResponses = itemRepository.findItemsByRequestId(itemRequest.getId()).stream()
                .map(ItemMapper::toItemOwnerDto)
                .toList();
        ItemRequestServiceImpl.log.info("Для ItemRequest = {} нашли ответы на запросы  {}", itemRequest.getId(), itemResponses);
        ItemRequestResponseOwnerDto itemRequestResponse = ItemRequestMapper.toItemRequestResponseOwnerDto(itemRequest, itemResponses);
        return itemRequestResponse;

    }

}
