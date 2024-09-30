package ru.practicum.shareit.item.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
public class ItemDto {
    long id;
    @NotEmpty(message = "Не может быть пустым")
    String name;
    @NotEmpty(message = "Не может быть пустым")
    String description;
    Boolean available;
    @Nullable
    Long requestId;
}