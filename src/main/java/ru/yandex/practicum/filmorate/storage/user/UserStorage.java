package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User findById(Long id);

    List<User> findAll();

    List<User> findByIds(Set<Long> ids);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
