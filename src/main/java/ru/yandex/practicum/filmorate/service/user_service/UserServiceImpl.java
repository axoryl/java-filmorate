package ru.yandex.practicum.filmorate.service.user_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.Validator.validateUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User getUserById(final Long id) {
        var user = userStorage.findById(id);

        throwExceptionIfNotExists(user);

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    @Override
    public List<User> getUserFriends(final Long id) {
        throwExceptionIfNotExists(userStorage.findById(id));
        Set<Long> friendsIds = userStorage.findById(id).getFriends();
        return userStorage.findByIds(friendsIds);
    }

    @Override
    public List<User> getMutualFriends(final Long id, final Long otherId) {
        final var user = userStorage.findById(id);
        final var otherUser = userStorage.findById(otherId);

        throwExceptionIfNotExists(user, otherUser);

        Set<Long> commonUserIds = user.getFriends()
                .stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());

        return userStorage.findByIds(commonUserIds);
    }

    @Override
    public User createUser(final User user) {
        final var validUser = validateUser(user);
        final var user2 = userStorage.findById(user.getId());
        if (user2 != null) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return userStorage.save(validUser);
    }

    @Override
    public User updateUser(final User user) {
        throwExceptionIfNotExists(userStorage.findById(user.getId()));
        return userStorage.update(user);
    }

    @Override
    public void addFriend(final Long id, final Long friendId) {
        final var user = userStorage.findById(id);
        final var userFriend = userStorage.findById(friendId);

        throwExceptionIfNotExists(user, userFriend);

        user.getFriends().add(friendId);
        userFriend.getFriends().add(id);
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteById(id);
    }

    @Override
    public void deleteFriend(final Long id, final Long friendId) {
        final var user = userStorage.findById(id);
        final var userFriend = userStorage.findById(friendId);

        throwExceptionIfNotExists(user, userFriend);

        user.getFriends().remove(friendId);
        userFriend.getFriends().remove(id);
    }

    private void throwExceptionIfNotExists(final User... users) {
        for (User user : users) {
            if (user == null) throw new NotFoundException("The user does not exist");
        }
    }
}
