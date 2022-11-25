package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    List<User> getMutualFriends(Long id, Long otherId);

    List<User> getUserFriends(Long id);

    User save(User user);

    void addFriend(Long userId, Long friendId);

    User update(User user);

    void deleteById(Long id);

    void deleteFriend(Long userId, Long friendId);
}
