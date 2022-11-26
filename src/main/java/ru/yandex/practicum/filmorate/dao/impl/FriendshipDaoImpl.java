package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Friendship> findFriendshipsByUserId(Long id) {
        final var sql = "SELECT * FROM friendship WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToFriendship, id);
    }

    @Override
    public void add(Long userId, Long friendId) {
        final var sql = "MERGE INTO friendship KEY (user_id, friend_id) " +
                "VALUES (?, ?, true)";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void delete(Long userId, Long friendId) {
        final var sql = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sql, userId, friendId);
    }

    private Friendship mapRowToFriendship(ResultSet rs, int rowNum) throws SQLException {
        return Friendship.builder()
                .userId(rs.getLong("user_id"))
                .friendId(rs.getLong("friend_id"))
                .status(rs.getBoolean("status"))
                .build();
    }
}
