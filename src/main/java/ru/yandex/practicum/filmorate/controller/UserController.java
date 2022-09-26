package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validator.Validator;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws BadRequestException, NotFoundException {
        final User validUser = Validator.validateUser(user);
        users.put(validUser.getId(), validUser);
        log.info("Create user");
        return users.get(validUser.getId());
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws BadRequestException, NotFoundException {
        final User validUser = Validator.validateUser(user);
        users.put(validUser.getId(), validUser);
        log.info("Update user");
        return users.get(validUser.getId());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
