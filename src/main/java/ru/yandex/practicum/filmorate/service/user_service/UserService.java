package ru.yandex.practicum.filmorate.service.user_service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    List<User> getAllUsers();

    List<User> getUserFriends(Long id);

    List<User> getMutualFriends(Long id, Long friendId);

    User createUser(User user);

    void addFriend(Long id, Long friendId);

    User updateUser(User user);

    void deleteUserById(Long id);

    void deleteFriend(Long id, Long friendId);
}
