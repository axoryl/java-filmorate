package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDaoTest {

    private final FilmGenreDao filmGenreDao;

    private final FilmDao filmDao;

    @Test
    @Order(1)
    public void testAdd() {
        Film film = getValidFilm();
        filmDao.save(film);

        FilmGenre filmGenre = FilmGenre.builder()
                .filmId(1L)
                .genreId(3L)
                .build();

        filmGenreDao.add(List.of(filmGenre));

        List<FilmGenre> filmGenres = filmGenreDao.findByFilmId(1L);

        assertThat(filmGenres)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @Order(2)
    public void testFindByFilmId() {
        FilmGenre filmGenre = FilmGenre.builder()
                .filmId(1L)
                .genreId(5L)
                .build();

        filmGenreDao.add(List.of(filmGenre));

        List<FilmGenre> filmGenres = filmGenreDao.findByFilmId(1L);

        assertThat(filmGenres)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Order(3)
    public void testDelete() {
        filmGenreDao.delete(1L);
        List<FilmGenre> filmGenres = filmGenreDao.findByFilmId(1L);

        assertThat(filmGenres).isEmpty();

    }
}
