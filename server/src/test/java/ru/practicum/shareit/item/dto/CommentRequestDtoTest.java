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
class CommentRequestDtoTest {
    private final JacksonTester<CommentRequestDto> json;

    @Test
    void createCommentRequestDtoTest() {
        CommentRequestDto commentRequestDto = new CommentRequestDto(
                "test text"
        );

        assertThat(commentRequestDto.getText(), equalTo("test text"));

    }
}
