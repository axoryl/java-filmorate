package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTest {

    private final MpaDao mpaDao;

    @Test
    public void testFindAll() {
        List<Mpa> mpa = mpaDao.findAll();

        assertThat(mpa)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    public void testFindMpaById() {
        Mpa mpa = mpaDao.findById(1L);

        assertThat(mpa)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "G");
    }
}