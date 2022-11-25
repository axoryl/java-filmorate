package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {

    private final GenreDao genreDao;

    @Test
    public void testFindByIds() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<Genre> genres = genreDao.findByIds(ids);

        assertThat(genres)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    public void testFindAll() {
        List<Genre> genres = genreDao.findAll();

        assertThat(genres)
                .isNotNull()
                .isNotEmpty()
                .hasSize(6);
    }

    @Test
    public void testFindById() {
        Genre genre = genreDao.findById(1L);

        assertThat(genre)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }
}
