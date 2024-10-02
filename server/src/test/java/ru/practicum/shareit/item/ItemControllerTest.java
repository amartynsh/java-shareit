package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exceptions.ExceptionsHandler;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController controller;
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemRequestDto itemRequestDto;
    private ItemRequestResponseDto itemRequestResponseDto;
    private ItemDatesCommentsDto itemDatesCommentsDto;
    private CommentResponseDto commentResponseDto;
    private DatesDto lastDatesDto;
    private DatesDto nextDatesDto;
    private CommentRequestDto commentRequestDto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ExceptionsHandler())
                .build();

        itemDto = new ItemDto(1002L, "Test Name", "Test Description", true, null);

        commentResponseDto = new CommentResponseDto(
                1L,
                "TestText",
                "TestAuthorName",
                LocalDateTime.now().toString());
        lastDatesDto = new DatesDto(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1));
        nextDatesDto = new DatesDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        itemRequestDto = new ItemRequestDto(
                "Test Description"
        );


        itemDatesCommentsDto = new ItemDatesCommentsDto(1002L,
                "test name",
                "test description",
                true,
                List.of(commentResponseDto),
                lastDatesDto,
                nextDatesDto);
        commentRequestDto = new CommentRequestDto("тестовый комментарий");
    }


    @Test
    void itemShouldBeAdded() throws Exception {
        when(itemService.addItem(itemDto, 1002L))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1002L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(1002L, 1002L))
                .thenReturn(itemDatesCommentsDto);

        mvc.perform(get("/items/{itemId}", itemDatesCommentsDto.getId())
                        .header("X-Sharer-User-Id", 1002L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(itemDatesCommentsDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDatesCommentsDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDatesCommentsDto.getDescription())));
    }

    @Test
    @DisplayName(value = "Поиск GET /items/search")
    void shouldFindItemsByTextTest() throws Exception {
        when(itemService.findItemByText(any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "test")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    void shouldNotUpdateItemsTest() throws Exception {

        mvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(null))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName(value = "POST /items/{itemId}/comment")
    void shouldAddCommentTest() throws Exception {
        when(itemService.addComment(commentRequestDto, 1L, 1L))
                .thenReturn(commentResponseDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentResponseDto.getId()), Long.class));
    }

    @Test
    void shoulBeBadRequest() throws Exception {
        mvc.perform(post("/item", 1L)
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mvc.perform(post("/items/", "test")
                        .content(mapper.writeValueAsString(null))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName(value = "Поиск GET /items/")
    void getItemsByOwnerId() throws Exception {
        when(itemService.getItemsByOwnerId(1))
                .thenReturn(List.of(itemDatesCommentsDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldUpdateItemsTest() throws Exception {
        when(itemService.getItemById(1001, 1002L))
                .thenReturn(itemDatesCommentsDto);

        mvc.perform(patch("/items/{itemId}", 1001L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1002L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
