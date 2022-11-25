package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.ExceptionThrowHandler.throwExceptionIfUserAlreadyExists;
import static ru.yandex.practicum.filmorate.util.ExceptionThrowHandler.throwExceptionIfUserNotExists;
import static ru.yandex.practicum.filmorate.validator.Validator.validateUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final FriendshipService friendshipService;

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public User getById(final Long id) {
        final var user = userDao.findById(id);
        throwExceptionIfUserNotExists(user);

        return user;
    }

    @Override
    public List<User> getMutualFriends(Long id, Long otherId) {
        final List<User> friends1 = getUserFriends(id);
        final List<User> friends2 = getUserFriends(otherId);

        return friends1.stream()
                .distinct()
                .filter(friends2::contains)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUserFriends(Long id) {
        throwExceptionIfUserNotExists(getById(id));
        final Set<Long> friendsId = new HashSet<>();

        for (Friendship friendship : friendshipService.getAllFriendships(id)) {
            friendsId.add(friendship.getFriendId());
        }

        return userDao.findByIds(friendsId);
    }

    @Override
    public User save(final User user) {
        final var validUser = validateUser(user);
        throwExceptionIfUserAlreadyExists(userDao.findById(validUser.getId()));

        return userDao.save(validUser);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        final var user = getById(userId);
        final var friend = getById(friendId);
        throwExceptionIfUserNotExists(user);
        throwExceptionIfUserNotExists(friend);

        friendshipService.addFriendship(userId, friendId);
    }

    @Override
    public User update(final User user) {
        throwExceptionIfUserNotExists(userDao.findById(user.getId()));

        return userDao.update(user);
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        friendshipService.deleteFriendship(userId, friendId);
    }
}
