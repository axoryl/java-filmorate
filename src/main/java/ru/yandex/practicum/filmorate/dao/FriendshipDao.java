package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipDao {

    List<Friendship> findFriendshipsByUserId(Long id);

    void add(Long userId, Long friendId);

    void delete(Long userId, Long friendId);
}
