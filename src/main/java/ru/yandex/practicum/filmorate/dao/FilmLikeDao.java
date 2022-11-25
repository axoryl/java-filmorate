package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface FilmLikeDao {

    List<Long> findMostLikedFilms(int limit);

    void add(Long filmId, Long userId);

    void delete(Long filmId, Long userId);
}
