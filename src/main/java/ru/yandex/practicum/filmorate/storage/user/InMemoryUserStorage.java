package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, User> usersStorage;

    @Override
    public User findById(final Long id) {
        return usersStorage.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(usersStorage.values());
    }

    @Override
    public List<User> findByIds(final Set<Long> ids) {
        List<User> users = new ArrayList<>();

        for (Long id : ids) {
            users.add(usersStorage.get(id));
        }
        return users;
    }

    @Override
    public User save(final User user) {
        user.setId(id.getAndIncrement());
        usersStorage.put(user.getId(), user);
        log.info("User created=[{}]", user);
        return user;
    }

    @Override
    public User update(final User user) {
        usersStorage.put(user.getId(), user);
        log.info("User updated=[{}]", user);
        return user;
    }

    @Override
    public void deleteById(final Long id) {
        usersStorage.remove(id);
    }
}
