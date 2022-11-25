package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findByIds(List<Long> genresId);

    List<Genre> findAll();

    Genre findById(Long id);
}
