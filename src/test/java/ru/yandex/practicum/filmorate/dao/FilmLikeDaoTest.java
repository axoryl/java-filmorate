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
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidFilm;
import static ru.yandex.practicum.filmorate.util.TestModel.getValidUser;

@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmLikeDaoTest {

    private final FilmLikeDao filmLikeDao;

    private final FilmDao filmDao;

    private final UserDao userDao;

    @Test
    @Order(1)
    public void testAdd() {
        User user = getValidUser();
        Film film = getValidFilm();
        userDao.save(user);
        filmDao.save(film);

        filmLikeDao.add(1L, 1L);

        List<Long> ids = filmLikeDao.findMostLikedFilms(10);

        assertThat(ids)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @Order(2)
    public void testFindMostLikedFilms() {
        Film film = getValidFilm();
        filmDao.save(film);

        filmLikeDao.add(2L, 1L);

        List<Long> ids = filmLikeDao.findMostLikedFilms(10);

        assertThat(ids)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    @Order(3)
    public void testDelete() {
        filmLikeDao.delete(1L, 1L);

        List<Long> ids = filmLikeDao.findMostLikedFilms(10);

        assertThat(ids)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }
}
