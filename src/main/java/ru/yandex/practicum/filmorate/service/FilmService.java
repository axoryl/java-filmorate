package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    List<Film> getAll();

    Film getById(Long id);

    List<Film> getMostLikedFilms(Integer count);

    Film save(Film film);

    void addLike(Long filmId, Long userId);

    Film update(Film film);

    void deleteById(Long id);

    void deleteLike(Long filmId, Long userId);
}
