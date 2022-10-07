package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class TestModel {
    public static Film getValidFilm() {
        return Film.builder()
                .id(null)
                .name("Test Name")
                .description("Test Description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
    }

    public static User getValidUser() {
       return User.builder().id(null)
                .email("test@mail.com")
                .login("TestLogin")
                .name("Test Name")
                .birthday(LocalDate.now().minusYears(25))
                .build();
    }
}
