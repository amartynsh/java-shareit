package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentResponseDtoTest {

    @Test
    void createCommentResponseDtoTest() {
        CommentResponseDto commentResponseDto = new CommentResponseDto(
                1L,
                "test text",
                "name",
                "created"
        );

        assertThat(commentResponseDto.getText(), equalTo("test text"));
        assertThat(commentResponseDto.getAuthorName(), equalTo("name"));
        assertThat(commentResponseDto.getCreated(), equalTo("created"));
        assertThat(commentResponseDto.getId(), equalTo(1L));
    }

}