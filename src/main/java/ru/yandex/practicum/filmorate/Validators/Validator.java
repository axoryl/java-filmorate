package ru.yandex.practicum.filmorate.Validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.Validators.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.Validators.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validator {
    public static User validateUser(User user) throws BadRequestException, NotFoundException {
        // Немного не понял зачем в тестах Postman'a, если id нет, то должно вернутся id == 1
        // Users - create
        // https://github.com/yandex-praktikum/java-filmorate/blob/controllers-films-users/postman/sprint.json
        if (user.getId() == null) {
            log.info("User id is null");
            user.setId(1L);
        }
        if (user.getId() < 1) {
            log.info("Incorrect id");
            throw new NotFoundException("Incorrect id");
        }
        if (user.getLogin().contains(" ")) {
            log.info("Invalid login");
            throw new BadRequestException("Invalid login");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Change name to login");
            user.setName(user.getLogin());
        }
        return user;
    }

    public static Film validateFilm(Film film) throws BadRequestException, NotFoundException {
        if (film.getId() == null) {
            log.info("Film id is null");
            film.setId(1L);
        }
        if (film.getId() < 1) {
            log.info("Incorrect id");
            throw new NotFoundException("Incorrect id");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Invalid movie release date");
            throw new BadRequestException("Invalid movie release date");
        }
        return film;
    }
}
