package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import ru.practicum.shareit.user.UserService;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemRequestDto itemRequestDto;
    private ItemRequestResponseDto itemRequestResponseDto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = new ItemDto(null, "Test Name", "Test Description",true, null);
    }



    @Test
    void itemShouldBeCreated() throws Exception {

    }

    @Test
    void getItem() {
    }

    @Test
    void getItemsByOwnerId() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void findItemsByText() {
    }

    @Test
    void addComment() {
    }
}