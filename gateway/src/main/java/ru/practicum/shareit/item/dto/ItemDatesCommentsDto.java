package ru.practicum.shareit.item.dto;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
public class ItemDatesCommentsDto {
    long id;
    String name;
    String description;
    Boolean available;
    List<CommentResponseDto> comments;
    @Nullable
    DatesDto lastBooking;
    @Nullable
    DatesDto nextBooking;
}