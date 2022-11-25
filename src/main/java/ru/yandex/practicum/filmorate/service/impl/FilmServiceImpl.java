package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmGenreService;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.ExceptionThrowHandler.*;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmDao filmDao;
    private final FilmLikeService filmLikeService;
    private final UserServiceImpl userService;
    private final FilmGenreService filmGenreService;
    private final GenreService genreService;

    @Override
    public List<Film> getAll() {
        return filmDao.findAll().stream()
                .map(this::setGenres)
                .collect(Collectors.toList());
    }

    @Override
    public Film getById(final Long id) {
        final var film = filmDao.findById(id);
        throwExceptionIfFilmNotExists(film);

        return setGenres(film);
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        List<Long> filmsId = filmLikeService.getMostLikedFilms(count);
        if (filmsId.isEmpty()) {
            return filmDao.findAllWithLimit(count);
        }

        return filmDao.findByIds(filmsId).stream()
                .map(this::setGenres)
                .collect(Collectors.toList());
    }

    @Override
    public Film save(final Film film) {
        final var filmForCheck = filmDao.findById(film.getId());
        throwExceptionIfFilmAlreadyExists(filmForCheck);
        final var createdFilm = filmDao.save(film);
        addGenres(film);

        return createdFilm;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        final var film = getById(filmId);
        final var user = userService.getById(userId);
        throwExceptionIfFilmNotExists(film);
        throwExceptionIfUserNotExists(user);

        filmLikeService.like(filmId, userId);
    }

    @Override
    public Film update(final Film film) {
        final var filmForCheck = filmDao.findById(film.getId());
        throwExceptionIfFilmNotExists(filmForCheck);

        filmGenreService.deleteGenresFromFilm(film.getId());
        addGenres(film);
        setGenres(film);
        return filmDao.update(film);
    }

    @Override
    public void deleteById(final Long id) {
        filmDao.deleteById(id);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        final var film = getById(filmId);
        final var user = userService.getById(userId);
        throwExceptionIfFilmNotExists(film);
        throwExceptionIfUserNotExists(user);

        filmLikeService.dislike(filmId, userId);
    }

    private Film setGenres(Film film) {
        List<Long> genreIds = filmGenreService.getByFilmId(film.getId())
                .stream()
                .map(FilmGenre::getGenreId)
                .collect(Collectors.toList());

        List<Genre> genres = genreService.getByIds(genreIds);
        film.setGenres(genres);
        return film;
    }

    private void addGenres(Film film) {
        if (film.getGenres() == null) return;

        final Set<Long> genresId = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        filmGenreService.addGenresToFilm(film.getId(), genresId);
    }
}