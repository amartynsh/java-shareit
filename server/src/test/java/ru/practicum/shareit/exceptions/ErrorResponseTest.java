package ru.practicum.shareit.exceptions;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ErrorResponseTest {
    @Test
    void createErrorResponseTest() {
        ErrorResponse errorResponse = new ErrorResponse("test text");
        assertThat(errorResponse.getError(), equalTo("test text"));
    }

}