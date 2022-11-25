package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipService {

    List<Friendship> getAllFriendships(Long id);

    void addFriendship(Long userId, Long friendId);

    void deleteFriendship(Long userId, Long friendId);
}
