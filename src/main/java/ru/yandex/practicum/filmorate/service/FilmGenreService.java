package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;
import java.util.Set;

public interface FilmGenreService {

    List<FilmGenre> getByFilmId(Long filmId);

    void addGenresToFilm(Long filmId, Set<Long> genresId);

    void deleteGenresFromFilm(Long filmId);
}
