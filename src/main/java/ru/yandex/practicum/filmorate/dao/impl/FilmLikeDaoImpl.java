package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmLikeDaoImpl implements FilmLikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> findMostLikedFilms(int limit) {
        final var sql = "SELECT film_id, COUNT(user_id) AS likes " +
                "FROM film_like " +
                "GROUP BY film_id " +
                "ORDER BY likes DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, this::rowMapToLong, limit);
    }

    @Override
    public void add(Long filmId, Long userId) {
        final var sql = "MERGE INTO film_like KEY (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        final var sql = "DELETE FROM film_like " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    private Long rowMapToLong(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("film_id");
    }
}
