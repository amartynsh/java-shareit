package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DatesDtoTest {
    @Test
    void createDatesDtoTest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime now2 = LocalDateTime.now();
        DatesDto datesDto = new DatesDto(
                now,
                now2);
        assertThat(datesDto.getStart(), equalTo(now));
        assertThat(datesDto.getEnd(), equalTo(now2));

    }
}