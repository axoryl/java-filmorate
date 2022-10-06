package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import ru.yandex.practicum.filmorate.validator.DateConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @DateConstraint(min = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @Singular
    private final Set<Long> likes = new HashSet<>();
}