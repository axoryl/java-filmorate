package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    List<Film> findAll();

    List<Film> findAllWithLimit(Integer limit);

    List<Film> findByIds(List<Long> filmsId);

    Film findById(Long id);

    Film save(Film film);

    Film update(Film film);

    void deleteById(Long id);
}
