package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.service.FilmGenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmGenreServiceImpl implements FilmGenreService {

    private final FilmGenreDao filmGenreDao;

    @Override
    public List<FilmGenre> getByFilmId(Long filmId) {
        return filmGenreDao.findByFilmId(filmId);
    }

    @Override
    public void addGenresToFilm(Long filmId, Set<Long> genresId) {
        List<FilmGenre> filmGenres = genresId.stream()
                .map(genreId -> FilmGenre.builder()
                        .filmId(filmId)
                        .genreId(genreId)
                        .build())
                .collect(Collectors.toList());

        if (!filmGenres.isEmpty()) {
            filmGenreDao.add(filmGenres);
        }
    }

    @Override
    public void deleteGenresFromFilm(Long filmId) {
        filmGenreDao.delete(filmId);
    }
}
