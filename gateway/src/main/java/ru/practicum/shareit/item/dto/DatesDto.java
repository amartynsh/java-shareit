package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class DatesDto {
    LocalDateTime start;
    LocalDateTime end;

    public int compareToStart(DatesDto datesDto) {
        return start.compareTo(datesDto.getStart());
    }

    public int compareToEnd(DatesDto datesDto) {
        return start.compareTo(datesDto.getEnd());
    }
}