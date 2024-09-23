package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String name;
    @Email(message = "Email is not valid")
    String email;
}