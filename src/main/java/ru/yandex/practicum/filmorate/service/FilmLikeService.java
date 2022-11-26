package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface FilmLikeService {

    List<Long> getMostLikedFilms(int count);

    void like(Long filmId, Long userId);

    void dislike(Long filmId, Long userId);
}
