package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validator.Validator;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws BadRequestException, NotFoundException {
        final Film validFilm = Validator.validateFilm(film);
        films.put(validFilm.getId(), validFilm);
        log.info("Create film");
        return films.get(validFilm.getId());
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws BadRequestException, NotFoundException {
        final Film validFilm = Validator.validateFilm(film);
        films.put(validFilm.getId(), validFilm);
        log.info("Update film");
        return films.get(validFilm.getId());
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
