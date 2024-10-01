package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseOwnerDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)

public class ItemRequestControllerTest {
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();
    private final ItemRequestResponseOwnerDto itemRequestResponseOwnerDto = new ItemRequestResponseOwnerDto(
            1L,
            "description",
            LocalDateTime.now(),
            List.of(new ItemOwnerDto(1L, "name", 1L))
    );
    private final ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto(
            1L,
            "description",
            LocalDateTime.now());
    private final ItemRequestDto itemRequestDto = new ItemRequestDto("description");
    @Mock
    ItemRequestServiceImpl itemRequestService;
    @InjectMocks
    ItemRequestController controller;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

    }

    @Test
    public void addRequest() throws Exception {
        when(itemRequestService.createRequest(itemRequestDto, 1L))
                .thenReturn(itemRequestResponseDto);

        mvc.perform(MockMvcRequestBuilders.post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    public void getRequestsOfUser() throws Exception {
        when(itemRequestService.getRequestByAuthorId(1L))
                .thenReturn(List.of(itemRequestResponseOwnerDto));

        mvc.perform(MockMvcRequestBuilders.get("/requests")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestResponseOwnerDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestResponseOwnerDto.getDescription())));
    }

    @Test
    public void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(1L))
                .thenReturn(itemRequestResponseOwnerDto);

        mvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseOwnerDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseOwnerDto.getDescription())));
    }
}