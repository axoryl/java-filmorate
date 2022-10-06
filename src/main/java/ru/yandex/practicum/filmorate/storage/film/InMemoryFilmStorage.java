package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, Film> films;

    @Override
    public Film findById(final Long id) {
        return films.get(id);
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film save(final Film film) {
        film.setId(id.getAndIncrement());
        films.put(film.getId(), film);
        log.info("Film created=[{}]", film);
        return film;
    }

    @Override
    public Film update(final Film film) {
        films.put(film.getId(), film);
        log.info("Film updated=[{}]", film);
        return film;
    }

    @Override
    public void deleteById(final Long id) {
        films.remove(id);
    }
}
