package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

public final class ExceptionThrowHandler {

    public static void throwExceptionIfUserNotExists(final User... users) {
        for (User user : users) {
            if (user == null) throw new NotFoundException("User does not exist");
        }
    }

    public static void throwExceptionIfFilmNotExists(final Film... films) {
        for (Film film : films) {
            if (film == null) throw new NotFoundException("Film does not exists");
        }
    }

    public static void throwExceptionIfGenreNotExists(final Genre... genres) {
        for (Genre genre : genres) {
            if (genre == null) throw new NotFoundException("Genre does not exist");
        }
    }

    public static void throwExceptionIfMpaNotExists(final Mpa... ratings) {
        for (Mpa mpa : ratings) {
            if (mpa == null) throw new NotFoundException("MPA does not exists");
        }
    }

    public static void throwExceptionIfUserAlreadyExists(final User... users) {
        for (User user : users) {
            if (user != null) throw new AlreadyExistsException("User already exists");
        }
    }

    public static void throwExceptionIfFilmAlreadyExists(final Film... films) {
        for (Film film : films) {
            if (film != null) throw new AlreadyExistsException("Film already exists");
        }
    }
}
