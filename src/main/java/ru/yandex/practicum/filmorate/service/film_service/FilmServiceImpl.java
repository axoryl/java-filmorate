package ru.yandex.practicum.filmorate.service.film_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film getFilmById(final Long id) {
        final var film = filmStorage.findById(id);
        throwExceptionIfNotExists(film);

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }
        final Comparator<Film> comparator = Comparator.comparingInt(o -> o.getLikes().size());

        return filmStorage.findAll()
                .stream()
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film createFilm(final Film film) {
        final var film2 = filmStorage.findById(film.getId());
        if (film2 != null) {
            throw new FilmAlreadyExistsException("Film already exists");
        }
        return filmStorage.save(film);
    }

    @Override
    public Film updateFilm(final Film film) {
        throwExceptionIfNotExists(filmStorage.findById(film.getId()));
        return filmStorage.update(film);
    }

    @Override
    public void deleteFilmById(final Long id) {
        filmStorage.deleteById(id);
    }

    @Override
    public void like(final Long filmId, final Long userId) {
        final var film = filmStorage.findById(filmId);
        final var user = userStorage.findById(userId);

        throwExceptionIfNotExists(film);
        if (user == null) {
            throw  new NotFoundException("The user does not exist");
        }

        film.getLikes().add(userId);
    }

    @Override
    public void deleteLike(final Long filmId, final Long userId) {
        final var film = filmStorage.findById(filmId);
        final var user = userStorage.findById(userId);

        throwExceptionIfNotExists(film);
        if (user == null) {
            throw  new NotFoundException("The user does not exist");
        }

        film.getLikes().remove(userId);
    }

    private void throwExceptionIfNotExists(final Film... films) {
        for (Film film : films) {
            if (film == null) throw new NotFoundException("The film does not exist");
        }
    }
}
