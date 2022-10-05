package ru.yandex.practicum.filmorate.service.film_service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film getFilmById(Long id);

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilmById(Long id);

    void like(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getMostLikedFilms(Integer count);
}
