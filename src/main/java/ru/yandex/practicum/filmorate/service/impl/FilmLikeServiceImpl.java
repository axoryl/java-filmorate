package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.service.FilmLikeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmLikeServiceImpl implements FilmLikeService {

    private final FilmLikeDao filmLikeDao;

    @Override
    public List<Long> getMostLikedFilms(int count) {
        return filmLikeDao.findMostLikedFilms(count);
    }

    @Override
    public void like(Long filmId, Long userId) {
        filmLikeDao.add(filmId, userId);
    }

    @Override
    public void dislike(Long filmId, Long userId) {
        filmLikeDao.delete(filmId, userId);
    }
}
