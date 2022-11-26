package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.util.ExceptionThrowHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    public List<Genre> getByIds(List<Long> genresId) {
        return genreDao.findByIds(genresId);
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.findAll();
    }

    @Override
    public Genre getById(Long id) {
        final Genre genre = genreDao.findById(id);
        ExceptionThrowHandler.throwExceptionIfGenreNotExists(genre);

        return genre;
    }
}
