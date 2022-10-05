package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import ru.yandex.practicum.filmorate.validator.NotContainsWhitespaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @NotContainsWhitespaces
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    @Singular
    private final Set<Long> friends = new HashSet<>();
}
