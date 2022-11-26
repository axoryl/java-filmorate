package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmGenre> findByFilmId(Long filmId) {
        final var sql = "SELECT * " +
                "FROM film_genre " +
                "WHERE film_id = ? " +
                "ORDER BY genre_id";
        return jdbcTemplate.query(sql, this::mapRowToFilmGenre, filmId);
    }

    @Override
    public void add(List<FilmGenre> filmGenres) {
        final var sql = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                filmGenres,
                100,
                (PreparedStatement ps, FilmGenre filmGenre) -> {
                    ps.setLong(1, filmGenre.getFilmId());
                    ps.setLong(2, filmGenre.getGenreId());
                });
    }

    @Override
    public void delete(Long filmId) {
        final var sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    private FilmGenre mapRowToFilmGenre(ResultSet rs, int rowNum) throws SQLException {
        return FilmGenre.builder()
                .filmId(rs.getLong("film_id"))
                .genreId(rs.getLong("genre_id"))
                .build();
    }
}
