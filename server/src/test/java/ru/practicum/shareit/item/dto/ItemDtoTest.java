package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    private final JacksonTester<ItemDto> json;

    @Test
    void itemDtoTest() throws Exception {
        ItemDto itemDto = new ItemDto(
                1L,
                "test item",
                "description",
                true,
                7L
        );

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo("test item"));
        assertThat(itemDto.getDescription(), equalTo("description"));
        assertThat(itemDto.getAvailable(), equalTo(true));
        assertThat(itemDto.getRequestId(), equalTo(7L));
    }
}