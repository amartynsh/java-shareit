package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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