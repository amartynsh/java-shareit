package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class User {
    int id;
    String name;
    @Email(message = "Email is not valid")
    String email;
}
