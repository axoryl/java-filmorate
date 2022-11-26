package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> getByIds(List<Long> genresId);

    List<Genre> getAll();

    Genre getById(Long id);
}
