package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
public class Validator {
    public static User validateUser(final User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Empty name[{}], change to login=[{}]", user.getName(), user.getLogin());
            user.setName(user.getLogin());
        }
        return user;
    }
}
