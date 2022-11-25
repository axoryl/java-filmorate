package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;

@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {

    private final FilmDao filmDao;

    @Test
    @Order(1)
    public void testSave() {
        Film film = filmDao.save(getValidFilm());
        Film response = filmDao.findById(film.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(2)
    public void testUpdate() {
        Film film = filmDao.findById(1L);
        film.setName("Updated");
        filmDao.update(film);

        Film response = filmDao.findById(film.getId());

        assertThat(response)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Updated");
    }

    @Test
    @Order(3)
    public void testFindAll() {
        Film film = getValidFilm();
        filmDao.save(film);

        List<Film> films = filmDao.findAll();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Order(4)
    public void testFindAllWithLimit() {
        List<Film> films = filmDao.findAllWithLimit(1);

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @Order(5)
    public void testFindByIds() {
        List<Film> films = filmDao.findByIds(List.of(1L, 2L));

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);

        Film film1 = films.get(0);
        Film film2 = films.get(1);

        assertThat(film1).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(film2).hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    @Order(6)
    public void testFindById() {
        Film film = filmDao.findById(1L);

        assertThat(film)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(7)
    public void testDeleteById() {
        filmDao.deleteById(1L);

        List<Film> films = filmDao.findAll();

        assertThat(films)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Film film = films.get(0);

        assertThat(film.getId()).isEqualTo(2L);
    }
}
