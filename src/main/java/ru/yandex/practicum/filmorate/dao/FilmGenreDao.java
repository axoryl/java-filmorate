package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreDao {

    List<FilmGenre> findByFilmId(Long filmId);

    void add(List<FilmGenre> filmGenres);

    void delete(Long filmId);
}
