package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDatesCommentsDtoTest {

    @Test
    void createItemDatesCommentsDtoTest() {
        CommentResponseDto commentResponseDto = new CommentResponseDto(
                1L,
                "TestText",
                "TestAuthorName",
                LocalDateTime.now().toString());
        DatesDto lastDatesDto = new DatesDto(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1));
        DatesDto nextDatesDto = new DatesDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));

        ItemDatesCommentsDto dto = new ItemDatesCommentsDto(
                1L,
                "test text",
                "name",
                true,
                List.of(commentResponseDto),
                lastDatesDto,
                nextDatesDto);

        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getName(), equalTo("test text"));
        assertThat(dto.getDescription(), equalTo("name"));
        assertThat(dto.getAvailable(), equalTo(true));
        assertThat(dto.getComments(), equalTo(List.of(commentResponseDto)));
        assertThat(dto.getLastBooking(), equalTo(lastDatesDto));
        assertThat(dto.getNextBooking(), equalTo(nextDatesDto));
    }
}