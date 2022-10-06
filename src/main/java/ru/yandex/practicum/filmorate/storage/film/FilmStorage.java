package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film findById(Long id);

    List<Film> findAll();

    Film save(Film film);

    Film update(Film film);

    void deleteById(Long id);
}
