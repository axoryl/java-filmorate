package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {

    List<User> findAll();

    List<User> findByIds(Set<Long> usersId);

    User findById(Long id);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
